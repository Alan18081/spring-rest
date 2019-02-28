package com.alex.springrest.models.response.errors;

import com.alex.springrest.models.response.ErrorResponse;
import org.springframework.http.HttpStatus;

public class BadRequestException extends ErrorResponse {

    public BadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST.value(), message);
    }
}
