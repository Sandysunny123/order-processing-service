package com.peer.orders.exception;


import java.time.Instant;

public class ApiErrorResponse implements ErrorResponse {

    private final int status;
    private final String error;
    private final String message;
    private final Instant timestamp = Instant.now();

    public ApiErrorResponse(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getError() {
        return error;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
