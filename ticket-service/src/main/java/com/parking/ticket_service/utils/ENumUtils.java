package com.parking.ticket_service.utils;

import com.parking.ticket_service.exception.AppException;
import com.parking.ticket_service.exception.ErrorCode;

import java.util.Locale;

public class ENumUtils {

    private ENumUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static <T extends Enum<T>> T getType(Class<T> enumType, String type) {
        type = type.toUpperCase(Locale.ROOT);

        try {
            return Enum.valueOf(enumType, type);
        } catch (Exception e) {
            throw new AppException(ErrorCode.INVALID_DATA);
        }
    }
}
