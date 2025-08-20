package com.nima.upquizz.exception;

import java.util.List;
import java.util.Map;

public record ValidationErrorResponse(
        int statusCode,
        String message,
        long timestamp,
        Map<String, List<String>> errors
) {

}