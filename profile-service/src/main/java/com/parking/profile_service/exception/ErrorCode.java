package com.parking.profile_service.exception;

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
    INVALID_PASSWORD(1001, "password is less than {min} characters", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    PROFILE_NOT_EXIST(1003, "Profile not exist", HttpStatus.BAD_REQUEST),
    WRONG_PASSWORD(1004, "Incorrect password", HttpStatus.BAD_REQUEST),
    AUTHENTICATION_FAIL(1005, "Incorrect username or password", HttpStatus.UNAUTHORIZED),
    ACCOUNT_BLOCKED(1006, "Account blocked", HttpStatus.FORBIDDEN),
    INVALID_EMAIL(1007, "Email format incorrect", HttpStatus.BAD_REQUEST),
    INVALID_KEY(1008, "Invalid message key", HttpStatus.BAD_REQUEST),
    BLANK_TOKEN(1009, "Token cannot be empty ", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1010, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    NOTFOUND_METHOD(1011, "Url not support method", HttpStatus.BAD_REQUEST),
    BODY_PARSE_FAIL(1012, "Body parse fail", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(1013, "Invalid token", HttpStatus.BAD_REQUEST),
    FIELD_INFORMATION_MISSING(1014, "Field information is missing: {field}", HttpStatus.BAD_REQUEST),
    CANNOT_DELETE(1015, "Cannot delete object", HttpStatus.BAD_REQUEST),
    NOTFOUND_URL(1016, "Url not exists", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(1017, "Not have permission", HttpStatus.FORBIDDEN),
    INVALID_BIRTHDAY(1018, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    INVALID_LIST_UID(1019, "The uid list must have at least {min} element", HttpStatus.BAD_REQUEST),
    PHONE_NUMBER_EXISTED(1020, "Phone number has been created", HttpStatus.BAD_REQUEST),
    INVALID_DATA(1021, "Invalid data, check again", HttpStatus.BAD_REQUEST),


    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    ;
    int code;
    String message;
    HttpStatusCode httpStatusCode;
}
