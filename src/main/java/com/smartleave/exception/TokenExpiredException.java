package com.smartleave.exception;

public class TokenExpiredException extends CustomException {
    public TokenExpiredException(String message) {
        super(message);
    }
}
