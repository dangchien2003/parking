package com.parking.ticket_service.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HttpClientException extends RuntimeException {
    final String response;

    public HttpClientException(String response) {
        this.response = response;
    }
}
