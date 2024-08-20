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
    INVALID_PASSWORD(4001, "password is less than {min} characters", HttpStatus.BAD_REQUEST),
    USER_EXISTED(4002, "User existed", HttpStatus.BAD_REQUEST),
    PROFILE_NOT_EXIST(4003, "Profile not exist", HttpStatus.BAD_REQUEST),
    WRONG_PASSWORD(4004, "Incorrect password", HttpStatus.BAD_REQUEST),
    AUTHENTICATION_FAIL(4005, "Incorrect username or password", HttpStatus.UNAUTHORIZED),
    ACCOUNT_BLOCKED(4006, "Account blocked", HttpStatus.FORBIDDEN),
    INVALID_EMAIL(4007, "Email format incorrect", HttpStatus.BAD_REQUEST),
    INVALID_KEY(4008, "Invalid message key", HttpStatus.BAD_REQUEST),
    BLANK_TOKEN(4009, "Token cannot be empty ", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(4010, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    NOTFOUND_METHOD(4011, "Url not support method", HttpStatus.BAD_REQUEST),
    BODY_PARSE_FAIL(4012, "Body parse fail", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(4013, "Invalid token", HttpStatus.BAD_REQUEST),
    FIELD_INFORMATION_MISSING(4014, "Field information is missing: {field}", HttpStatus.BAD_REQUEST),
    CANNOT_DELETE(4015, "Cannot delete object", HttpStatus.BAD_REQUEST),
    NOTFOUND_URL(4016, "Url not exists", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(4017, "Not have permission", HttpStatus.FORBIDDEN),
    INVALID_BIRTHDAY(4018, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    INVALID_LIST_UID(4019, "The uid list must have at least {min} element", HttpStatus.BAD_REQUEST),
    PHONE_NUMBER_EXISTED(4020, "Phone number has been created", HttpStatus.BAD_REQUEST),
    INVALID_DATA(4021, "Invalid data, check again", HttpStatus.BAD_REQUEST),


    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    ;
    int code;
    String message;
    HttpStatusCode httpStatusCode;
}
