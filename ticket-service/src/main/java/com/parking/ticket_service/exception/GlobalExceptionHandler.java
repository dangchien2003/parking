package com.parking.ticket_service.exception;


import com.parking.ticket_service.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.*;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private static final String[] KEY_ATTRIBUTE = {
            "min",
    };

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse<Object>> handlingException(Exception e) {
        log.error("error: ", e);
        ErrorCode errorCode = ErrorCode.UNCATEGORIZED_EXCEPTION;
        return setResponse(errorCode);
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    ResponseEntity<ApiResponse<Object>> handlingMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        ErrorCode errorCode = ErrorCode.INVALID_DATA;
        return setResponse(errorCode);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse<Object>> handlingAppException(AppException e) {
        ErrorCode errorCode = e.getErrorCode();
        return setResponse(errorCode);
    }

    @ExceptionHandler(value = HttpClientException.class)
    ResponseEntity<String> handlingHttpClientException(HttpClientException e) {
        String apiResponse = e.getResponse();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(apiResponse);
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    ResponseEntity<ApiResponse<Object>> handlingHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        ErrorCode errorCode = ErrorCode.BODY_PARSE_FAIL;
        return setResponse(errorCode);
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    ResponseEntity<ApiResponse<Object>> handlingHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        ErrorCode errorCode = ErrorCode.NOTFOUND_METHOD;
        return setResponse(errorCode);
    }


    @ExceptionHandler(value = HandlerMethodValidationException.class)
    ResponseEntity<ApiResponse<Object>> handlingHandlerMethodValidationException(HandlerMethodValidationException e) {
        ErrorCode errorCode = ErrorCode.INVALID_DATA;
        return setResponse(errorCode);
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    ResponseEntity<ApiResponse<Object>> handlingDataIntegrityViolationException(DataIntegrityViolationException e) {
        ErrorCode errorCode = ErrorCode.DATA_EXISTED;
        return setResponse(errorCode);
    }

    @ExceptionHandler(value = NoResourceFoundException.class)
    ResponseEntity<ApiResponse<Object>> handlingNoResourceFoundException(NoResourceFoundException e) {
        ErrorCode errorCode = ErrorCode.NOTFOUND_URL;
        return setResponse(errorCode);
    }

    @ExceptionHandler(value = AuthorizationDeniedException.class)
    ResponseEntity<ApiResponse<Object>> handlingAuthorizationDeniedException(AuthorizationDeniedException e) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        return setResponse(errorCode);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse<Object>> handlingMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        String message = errorCode.getMessage();

        String enumKey = e.getFieldError().getDefaultMessage();

        Map<String, Object> attributes;

        try {
            errorCode = ErrorCode.valueOf(enumKey);

            List<ObjectError> errorFields = e.getBindingResult()
                    .getAllErrors();

            if (errorCode.equals(ErrorCode.FIELD_INFORMATION_MISSING)) {

                message = mapErrorField(errorCode.getMessage(), errorFields);

            } else {

                var constrainViolation = errorFields.getFirst().unwrap(ConstraintViolation.class);
                attributes = constrainViolation.getConstraintDescriptor().getAttributes();
                message = Objects.isNull(attributes)
                        ? errorCode.getMessage()
                        : mapAttribute(errorCode.getMessage(), attributes);
            }
        } catch (Exception exception) {
            log.warn("Key not exist: {}", enumKey);
        }

        return ResponseEntity
                .status(errorCode.getHttpStatusCode())
                .body(
                        ApiResponse.builder()
                                .code(errorCode.getCode())
                                .message(message)
                                .build()
                );
    }

    String mapErrorField(String message, List<ObjectError> objectErrors) {
        StringJoiner stringJoiner = new StringJoiner(", ");

        objectErrors.forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            stringJoiner.add(fieldName);
        });

        message = message.replace("{field}", stringJoiner.toString());
        return message;
    }

    String mapAttribute(String message, Map<String, Object> attributes) {
        String[] result = {message};
        Arrays.stream(KEY_ATTRIBUTE).forEach(key -> {
            String keyValue = attributes.get(key) != null ? attributes.get(key).toString() : "";
            result[0] = result[0].replace("{" + key + "}", keyValue);
        });
        return result[0];
    }

    ResponseEntity<ApiResponse<Object>> setResponse(ErrorCode errorCode) {
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message(errorCode.getMessage())
                .code(errorCode.getCode())
                .build();
        return ResponseEntity
                .status(errorCode.getHttpStatusCode())
                .body(apiResponse);
    }
}
