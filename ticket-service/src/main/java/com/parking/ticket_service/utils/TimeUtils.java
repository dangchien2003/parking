package com.parking.ticket_service.utils;

import com.parking.ticket_service.exception.AppException;
import com.parking.ticket_service.exception.ErrorCode;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TimeUtils {
    public static boolean isValidDateTime(String dateTimeStr, String format) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
        try {
            LocalDateTime.parse(dateTimeStr, dtf);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static long timeToLong(String time, String format) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
        try {
            LocalDateTime dateTime = LocalDateTime.parse(time, dtf);
            return dateTime.atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant().toEpochMilli();
        } catch (DateTimeParseException e) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }
}
