package com.parking.vault_service.exception;

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
    OWNER_NOT_EXIST(1003, "Owner create not yet", HttpStatus.BAD_REQUEST),
    INVALID_KEY(1008, "Invalid message key", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1010, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    NOTFOUND_METHOD(1011, "Url not support method", HttpStatus.BAD_REQUEST),
    BODY_PARSE_FAIL(1012, "Body parse fail", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(1013, "Invalid token", HttpStatus.BAD_REQUEST),
    FIELD_INFORMATION_MISSING(1014, "Field information is missing: {field}", HttpStatus.BAD_REQUEST),
    NOTFOUND_URL(1016, "Url not exists", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(1017, "Not have permission", HttpStatus.FORBIDDEN),
    TOO_SMALL_AMOUNT(1020, "Deposit amount too small", HttpStatus.BAD_REQUEST),
    PARAM_MISSING(1021, "Param is missing", HttpStatus.BAD_REQUEST),
    INCORRECT_PARAM_FORMAT(1022, "Param is not in correct format", HttpStatus.BAD_REQUEST),
    INCORRECT_DATA(1023, "Invalid data", HttpStatus.BAD_REQUEST),
    UNSUPPORTED_FILTERS(1024, "Unsupported Filters", HttpStatus.BAD_REQUEST),
    TYPE_NOT_EXIST(1025, "Not understanding the action to be taken", HttpStatus.BAD_REQUEST),
    UPDATE_FAIL(1026, "Update failed", HttpStatus.BAD_REQUEST),
    LENGTH_TOO_SHORT(1027, "Data must be at least: {min}", HttpStatus.BAD_REQUEST),
    WALLET_IN_STATUS(1028, "Wallet is in status", HttpStatus.BAD_REQUEST),
    DEPOSIT_NOT_EXIST_OR_BEEN_APPROVED(1029, "Deposit does not exist or has been approved", HttpStatus.BAD_REQUEST),

    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    ;
    int code;
    String message;
    HttpStatusCode httpStatusCode;
}
