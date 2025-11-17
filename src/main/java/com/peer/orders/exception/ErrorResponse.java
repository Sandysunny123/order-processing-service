package com.peer.orders.exception;

public interface ErrorResponse {
    int getStatus();
    String getError();
    String getMessage();
}
