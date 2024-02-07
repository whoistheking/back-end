package com.example.demo.global.exception;

public class CustomTokenException extends RuntimeException {

    public CustomTokenException(String message) {
        super(message);
    }

    public CustomTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
