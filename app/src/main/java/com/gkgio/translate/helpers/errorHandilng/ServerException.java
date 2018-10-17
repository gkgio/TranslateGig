package com.gkgio.translate.helpers.errorHandilng;


public class ServerException extends RuntimeException {

    public final int httpCode;
    public final int code;

    ServerException(int httpCode, String message, int code) {
        super(message);
        this.httpCode = httpCode;
        this.code = code;
    }

}
