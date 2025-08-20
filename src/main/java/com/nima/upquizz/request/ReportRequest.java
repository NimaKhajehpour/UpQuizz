package com.nima.upquizz.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record ReportRequest(

        @NotEmpty(message = "report cant be empty")
        @Size(min = 10, message = "report must be at least 10 characters long")
        String report
) {
}
