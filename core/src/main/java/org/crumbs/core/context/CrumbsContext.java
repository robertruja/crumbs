package org.crumbs.core.context;

import org.crumbs.core.annotation.Crumb;
import org.crumbs.core.annotation.CrumbInit;
import org.crumbs.core.annotation.CrumbRef;
import org.crumbs.core.annotation.Property;
import org.crumbs.core.exception.CrumbsInitException;
import org.crumbs.core.logging.Logger;
import org.crumbs.core.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.time.Duration;
import java.util.*;

public class CrumbsContext {

    private static final Logger LOGGER = Logger.getLogger(CrumbsContext.class);

    private final Map<Class<?>, Object> crumbs = new HashMap<>();
    private Map<String, String> properties;

    void initialize(Class<?> clazz) throws Exception {
        LOGGER.info("Starting Crumbs Context ...");
        long start = System.currentTimeMillis();
        properties = ConfigLoader.loadProperties();
        loadCrumbs("org.crumbs", clazz.getPackage().getName());
        injectReferences();
        if (properties != null) {
            injectProperties();
        }
        initCrumbs();
        LOGGER.info("Crumbs context initialized in {} millis", System.currentTimeMillis() - start);
    }

    private void initCrumbs() {
        crumbs.values().forEach(crumb -> Arrays.stream(crumb.getClass().getDeclaredMethods()).forEach(method -> {
            Arrays.stream(method.getAnnotations())
                    .filter(annotation -> annotation.annotationType().equals(CrumbInit.class))
                    .findFirst()
                    .ifPresent(ann -> {
                        method.setAccessible(true);
                        try {
                            method.invoke(crumb);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new CrumbsInitException("Failed to call init methods on crumb "
                                    + crumb.getClass().getCanonicalName(), e);
                        }
                    });
        }));
    }

    private void injectProperties() {
        crumbs.values().forEach(crumb -> Arrays.stream(crumb.getClass().getDeclaredFields()).forEach(field -> {
            Arrays.stream(field.getAnnotations())
                    .filter(annotation -> annotation.annotationType().equals(Property.class))
                    .findFirst()
                    .ifPresent(annotation -> {
                        field.setAccessible(true);
                        Property property = field.getAnnotation(Property.class);
                        String propertyKey = property.value();
                        String value = properties.get(propertyKey);
                        if (value == null) {
                            return;
                        }
                        try {
                            Class<?> type = field.getType();
                            if (type.equals(String.class)) {
                                field.set(crumb, value);
                            } else if (type.equals(Integer.class)) {
                                Integer intValue = Integer.parseInt(value);
                                field.set(crumb, intValue);
                            } else if (type.equals(Long.class)) {
                                Long longValue = Long.parseLong(value);
                                field.set(crumb, longValue);
                            } else if (type.equals(Double.class)) {
                                Double doubleValue = Double.parseDouble(value);
                                field.set(crumb, doubleValue);
                            } else if (type.equals(Boolean.class)) {
                                Boolean boolValue = Boolean.parseBoolean(value);
                                field.set(crumb, boolValue);
                            } else if (type.equals(Duration.class)) {
                                Duration duration = Duration.parse(value);
                                field.set(crumb, duration);
                            } else {
                                throw new CrumbsInitException("Could not inject value in field " + field.getName() +
                                        " of type " + type.getCanonicalName() + " in class "
                                        + crumb.getClass().getCanonicalName() + ". Unsupported property type");
                            }
                            field.setAccessible(false);
                        } catch (IllegalAccessException e) {
                            throw new CrumbsInitException("Unable to set field value due to exception", e);
                        }
                    });
        }));
    }

    public <T> T getCrumb(Class<T> clazz) {
        return (T) crumbs.get(clazz);
    }

    public String getProperty(String key) {
        return properties.get(key);
    }

    public <U extends Annotation> List<?> getCrumbsWithAnnotation(Class<U> clazz) {
        if (!clazz.isAnnotation()) {
            throw new RuntimeException("Class of type: " + clazz.getName() + " is not an annotation");
        }
        List<Object> annotated = new ArrayList<>();
        for (Map.Entry<Class<?>, ?> entry : crumbs.entrySet()) {
            Class<?> key = entry.getKey();
            if (key.getAnnotation(clazz) != null) {
                annotated.add(entry.getValue());
            }
        }
        return annotated;
    }

    public <T> List<T> getCrumbsWithInterface(Class<T> clazz) {
        List<T> list = new ArrayList<>();
        for (Class<?> type : crumbs.keySet()) {
            if (clazz.isAssignableFrom(type)) {
                list.add((T) crumbs.get(type));
            }
        }
        return list;
    }

    private void injectReferences() {
        crumbs.values().forEach(crumb -> {
            Arrays.stream(crumb.getClass().getDeclaredFields()).forEach(field -> {
                if (Arrays.stream(field.getAnnotations())
                        .anyMatch(annotation -> annotation.annotationType().equals(CrumbRef.class))) {
                    field.setAccessible(true);
                    try {
                        Class<?> fieldType = field.getType();
                        Object value;

                        if (fieldType.isAssignableFrom(CrumbsContext.class)) {
                            value = this;
                        } else {
                            value = crumbs.get(field.getType());
                        }
                        if (value == null) {
                            value = getCrumbsWithInterface(fieldType)
                                    .stream()
                                    .findFirst()
                                    .orElseThrow(() ->
                                            new CrumbsInitException("Could not inject reference in object of type "
                                                    + crumb.getClass().getCanonicalName() +
                                                    ". No Crumbs of type " + field.getType().getCanonicalName() + " found. "));
                        }
                        field.set(crumb, value);
                        field.setAccessible(false);
                    } catch (IllegalAccessException e) {
                        throw new CrumbsInitException("Unable to set field value due to exception", e);
                    }
                }
            });
        });
    }

    private void loadCrumbs(String... packageNames) throws Exception {

        List<Class<?>> scannedClasses = new ArrayList<>();
        for (String name : packageNames) {
            scannedClasses.addAll(Scanner.getClassesInPackage(name));
        }

        scannedClasses.stream()
                .filter(scannedClazz -> ReflectionUtils.hasAnnotation(scannedClazz, Crumb.class))
                .filter(clazz -> !clazz.isAnnotation() && !clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers()))
                .forEach(clazz -> {
                    try {
                        Constructor constructor = clazz.getDeclaredConstructor();
                        constructor.setAccessible(true);
                        Object instance = constructor.newInstance();
                        crumbs.put(clazz, instance);
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        throw new CrumbsInitException("Error occurred on load or scan for crumbs", e);
                    }
                });
    }
}
