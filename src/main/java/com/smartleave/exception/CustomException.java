package com.smartleave.exception;

public class CustomException extends RuntimeException {
    public int statusCode;

    public CustomException(String message) {
        super(message);
    }
     
    public CustomException(String message, Throwable cause) {
        super(message,cause);
     }
}
