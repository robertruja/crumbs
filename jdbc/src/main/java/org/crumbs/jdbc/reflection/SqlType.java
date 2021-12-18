package org.crumbs.jdbc.reflection;

import java.sql.Timestamp;

public enum SqlType {
    GENERIC,
    BOOLEAN,
    STRING,
    INTEGER,
    LONG,
    DOUBLE,
    FLOAT,
    TIMESTAMP;

    public static SqlType fromClass(Class<?> clazz) {
        if(clazz.equals(Boolean.class) || clazz.equals(boolean.class)){
            return BOOLEAN;
        }
        else if(clazz.equals(String.class)) {
            return STRING;
        } else if (clazz.equals(Integer.class) || clazz.equals(int.class)){
            return INTEGER;
        } else if (clazz.equals(Long.class) || clazz.equals(long.class)){
            return LONG;
        } else if (clazz.equals(Double.class) || clazz.equals(double.class)){
            return DOUBLE;
        } else if (clazz.equals(Float.class) || clazz.equals(float.class)){
            return FLOAT;
        } else if (clazz.equals(Timestamp.class)) {
            return TIMESTAMP;
        }
        return GENERIC;
    }
}
