package com.exceptions;

import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

class ApiError {

    private HttpStatus status;
    private long timestamp;
    private String message;
    private List<String> errors;

    ApiError(HttpStatus status, long timestamp, String message, List<String> errors) {
        super();
        this.status = status;
        this.timestamp = timestamp;
        this.message = message;
        this.errors = errors;
    }

    ApiError(HttpStatus status, long timestamp, String message, String error) {
        super();
        this.status = status;
        this.timestamp = timestamp;
        this.message = message;
        errors = Collections.singletonList(error);
    }
}
