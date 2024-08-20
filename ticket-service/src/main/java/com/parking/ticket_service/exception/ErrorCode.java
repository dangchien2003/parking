package com.parking.ticket_service.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;


@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public enum ErrorCode {
    INVALID_KEY(6008, "Invalid message key", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(6010, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    NOTFOUND_METHOD(6011, "Url not support method", HttpStatus.BAD_REQUEST),
    BODY_PARSE_FAIL(6012, "Body parse fail", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(6013, "Invalid token", HttpStatus.BAD_REQUEST),
    FIELD_INFORMATION_MISSING(6014, "Field information is missing: {field}", HttpStatus.BAD_REQUEST),
    CANNOT_DELETE(6015, "Cannot delete object", HttpStatus.BAD_REQUEST),
    NOTFOUND_URL(6016, "Url not exists", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(6017, "Not have permission", HttpStatus.FORBIDDEN),
    INVALID_DATA(6021, "Invalid data, check again", HttpStatus.BAD_REQUEST),
    ADDRESS_EXISTED(6022, "Station already exists", HttpStatus.BAD_REQUEST),
    NOTFOUND_FILTER(6023, "Filter does not exist", HttpStatus.BAD_REQUEST),
    STATION_NOT_EXIST(6024, "Station not found", HttpStatus.BAD_REQUEST),
    UPDATE_FAIL(6025, "Update fail", HttpStatus.BAD_REQUEST),
    DATA_EXISTED(6026, "Data already exists", HttpStatus.BAD_REQUEST),
    DATA_NOT_FOUND(6027, "Data not yet initialized", HttpStatus.BAD_REQUEST),
    STATION_NOT_FOUND(6028, "Station not yet initialized", HttpStatus.BAD_REQUEST),
    INVALID_ADDRESS(6029, "Invalid address", HttpStatus.BAD_REQUEST),
    INVALID_CATEGORY(6030, "Invalid category", HttpStatus.BAD_REQUEST),
    CANNOT_BUY_TICKET(6030, "Can't buy tickets", HttpStatus.BAD_REQUEST),
    NOTFOUND_CATEGORY_UNIT(6031, "Category unit not found", HttpStatus.BAD_REQUEST),
    INSUFFICIENT_BALANCE(6032, "Insufficient Balance", HttpStatus.BAD_REQUEST),
    PROCESS_FAIL(6033, "Process fail", HttpStatus.BAD_REQUEST),


    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    ;
    int code;
    String message;
    HttpStatusCode httpStatusCode;
}
