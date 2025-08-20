package com.nima.upquizz.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record TagRequest(

        @NotEmpty(message = "Name is mandatory")
        @Size(min = 3, max = 20, message = "Name should be between 3 and 20 characters long")
        String name
) {
}
