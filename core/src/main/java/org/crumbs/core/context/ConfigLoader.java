package org.crumbs.core.context;

import org.crumbs.core.exception.CrumbsInitException;
import org.crumbs.core.logging.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class ConfigLoader {

    private static Logger LOGGER = Logger.getLogger(ConfigLoader.class);

    public static Map<String, String> loadProperties() {
        envToSystemProperties();
        Map<String, String> systemProperties = System.getProperties().keySet().stream()
                .collect(Collectors.toMap(Object::toString, key -> System.getProperty(key.toString())));

        InputStream propertiesStream = ConfigLoader.class.getClassLoader().getResourceAsStream("crumbs.properties");
        if (propertiesStream != null) {
            LOGGER.info("Found crumbs.properties, loading config");
            try {
                Properties properties = new Properties();
                properties.load(propertiesStream);
                Map<String, String> fileProperties = properties.keySet().stream()
                        .collect(Collectors.toMap(Object::toString, key -> properties.get(key).toString()));
                Map<String, String> replaced = replaceValues(fileProperties);
                replaced.putAll(systemProperties);
                return Collections.unmodifiableMap(replaced);
            } catch (IOException e) {
                throw new CrumbsInitException("Unable to load crumbs properties", e);
            }
        } else {
            LOGGER.warn("No crumbs properties found in classpath");
            return Collections.unmodifiableMap(systemProperties);
        }
    }

    private static void envToSystemProperties() {
        System.getenv().forEach((key, val) -> {
            System.setProperty(key.toLowerCase().replace("_", "."), val);
        });
    }

    private static Map<String, String> replaceValues(Map<String, String> propertyMap) {
        Map<String, String> replaced = propertyMap.keySet().stream()
                .collect(Collectors.toMap(key -> key, key -> System.getProperty(key, propertyMap.get(key))));
        return replaced.keySet()
                .stream()
                .collect(Collectors.toMap(key -> key, propertyKey -> replace(replaced.get(propertyKey), replaced)));
    }

    private static String replace(String propertyValue, Map<String, String> properties) {
        int lastIndex = 0;
        while ((lastIndex = propertyValue.indexOf("${")) != -1) {
            if (propertyValue.substring(lastIndex).contains("}")) {
                int end = propertyValue.indexOf("}");
                String prefix = propertyValue.substring(0, lastIndex);
                String ref = propertyValue.substring(lastIndex + 2, end);
                String suffix = propertyValue.substring(end + 1);
                propertyValue = prefix + properties.get(ref) + suffix;
            }
        }
        return propertyValue;
    }
}
