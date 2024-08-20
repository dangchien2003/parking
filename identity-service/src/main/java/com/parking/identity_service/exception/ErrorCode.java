package com.parking.identity_service.exception;

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
    INVALID_PASSWORD(2001, "password is less than {min} characters", HttpStatus.BAD_REQUEST),
    USER_EXISTED(2002, "User existed", HttpStatus.BAD_REQUEST),
    USER_NOT_EXIST(2003, "User not exist", HttpStatus.BAD_REQUEST),
    WRONG_PASSWORD(2004, "Incorrect password", HttpStatus.BAD_REQUEST),
    AUTHENTICATION_FAIL(2005, "Incorrect username or password", HttpStatus.UNAUTHORIZED),
    ACCOUNT_BLOCKED(2006, "Account blocked", HttpStatus.FORBIDDEN),
    INVALID_EMAIL(2007, "Email format incorrect", HttpStatus.BAD_REQUEST),
    INVALID_KEY(2008, "Invalid message key", HttpStatus.BAD_REQUEST),
    BLANK_TOKEN(2009, "Token cannot be empty ", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(2010, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    NOTFOUND_METHOD(2011, "Url not support method", HttpStatus.BAD_REQUEST),
    BODY_PARSE_FAIL(2012, "Body parse fail", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(2013, "Invalid token", HttpStatus.BAD_REQUEST),
    FIELD_INFORMATION_MISSING(2014, "Field information is missing: {field}", HttpStatus.BAD_REQUEST),
    CANNOT_DELETE(2015, "Cannot delete object", HttpStatus.BAD_REQUEST),
    NOTFOUND_URL(2016, "Url not exists", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(2017, "Not have permission", HttpStatus.FORBIDDEN),
    INVALID_BIRTHDAY(2018, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    INVALID_LIST_UID(2019, "The uid list must have at least {min} element", HttpStatus.BAD_REQUEST),


    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    ;
    int code;
    String message;
    HttpStatusCode httpStatusCode;
}
