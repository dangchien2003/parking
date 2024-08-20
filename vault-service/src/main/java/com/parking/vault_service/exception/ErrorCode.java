package com.parking.vault_service.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;


@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public enum ErrorCode {
    OWNER_NOT_EXIST(5003, "Owner create not yet", HttpStatus.BAD_REQUEST),
    INVALID_KEY(5008, "Invalid message key", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(5010, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    NOTFOUND_METHOD(5011, "Url not support method", HttpStatus.BAD_REQUEST),
    BODY_PARSE_FAIL(5012, "Body parse fail", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(5013, "Invalid token", HttpStatus.BAD_REQUEST),
    FIELD_INFORMATION_MISSING(5014, "Field information is missing: {field}", HttpStatus.BAD_REQUEST),
    NOTFOUND_URL(5016, "Url not exists", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(5017, "Not have permission", HttpStatus.FORBIDDEN),
    TOO_SMALL_AMOUNT(5020, "Deposit amount too small", HttpStatus.BAD_REQUEST),
    PARAM_MISSING(5021, "Param is missing", HttpStatus.BAD_REQUEST),
    INCORRECT_PARAM_FORMAT(5022, "Param is not in correct format", HttpStatus.BAD_REQUEST),
    INCORRECT_DATA(5023, "Invalid data", HttpStatus.BAD_REQUEST),
    UNSUPPORTED_FILTERS(5024, "Unsupported Filters", HttpStatus.BAD_REQUEST),
    TYPE_NOT_EXIST(5025, "Not understanding the action to be taken", HttpStatus.BAD_REQUEST),
    UPDATE_FAIL(5026, "Update failed", HttpStatus.BAD_REQUEST),
    LENGTH_TOO_SHORT(5027, "Data must be at least: {min}", HttpStatus.BAD_REQUEST),
    WALLET_IN_STATUS(5028, "Wallet is in status", HttpStatus.BAD_REQUEST),
    DEPOSIT_NOT_EXIST_OR_BEEN_APPROVED(5029, "Deposit does not exist or has been approved", HttpStatus.BAD_REQUEST),
    NOT_FOUND_WALLET(5030, "Wallet does not exist", HttpStatus.BAD_REQUEST),
    CANNOT_USE_WALLET(5031, "Wallet cannot be used", HttpStatus.BAD_REQUEST),
    DEPOSIT_FAIL(5032, "Deposit failed", HttpStatus.BAD_REQUEST),
    INVALID_DATA(5033, "Invalid data", HttpStatus.BAD_REQUEST),

    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    ;
    int code;
    String message;
    HttpStatusCode httpStatusCode;
}
