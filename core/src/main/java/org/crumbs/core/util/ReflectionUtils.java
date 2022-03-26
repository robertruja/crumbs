package org.crumbs.core.util;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

public class ReflectionUtils {
    /**
     * Check recursively whether the
     *
     * @param target     class has an superclass, interface or annotation annotated with
     * @param annotation
     */
    public static boolean hasAnnotation(Class<?> target, Class<? extends Annotation> annotation) {
        for (Annotation ann : target.getAnnotations()) {
            Class<?> clazz = ann.annotationType();
            if (Documented.class.equals(clazz) || Retention.class.equals(clazz) || Target.class.equals(clazz)) {
                continue;
            }
            if (ann.annotationType().equals(annotation) || hasAnnotation(ann.annotationType(), annotation)) {
                return true;
            }
        }
        for (Class<?> iface : target.getInterfaces()) {
            if (hasAnnotation(iface, annotation)) {
                return true;
            }
        }
        if (target.getSuperclass() != null && hasAnnotation(target.getSuperclass(), annotation)) {
            return true;
        }
        return false;
    }
}
