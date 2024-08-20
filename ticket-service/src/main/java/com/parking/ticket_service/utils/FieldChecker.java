package com.parking.ticket_service.utils;

import java.lang.reflect.Field;
import java.util.Objects;

public class FieldChecker {

    private FieldChecker() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static boolean hasField(Class<?> clazz, String fieldName) {
        if (Objects.isNull(fieldName))
            return false;

        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            String fieldNameInClass = field.getName();
            if (fieldNameInClass.length() == fieldName.length()
                    && fieldNameInClass.substring(0, 1).equalsIgnoreCase(fieldName.substring(0, 1))
                    && fieldNameInClass.substring(1).equals(fieldName.substring(1))) {
                return true;
            }
        }

        return false;
    }
}
