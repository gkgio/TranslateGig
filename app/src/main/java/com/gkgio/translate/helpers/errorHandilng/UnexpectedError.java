package com.gkgio.translate.helpers.errorHandilng;


public class UnexpectedError extends RuntimeException {
    public UnexpectedError(String message, Throwable cause) {
        super(message, cause);
    }
}
