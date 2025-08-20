package com.nima.upquizz.exception;

public record ExceptionResponse(
        int statusCode,
        String message,
        long timestamp
) {

}