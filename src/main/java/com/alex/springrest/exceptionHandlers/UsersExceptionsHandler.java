package com.alex.springrest.exceptionHandlers;

import com.alex.springrest.exceptions.UserServiceException;
import com.alex.springrest.models.response.ErrorResponse;
import com.alex.springrest.models.response.errors.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UsersExceptionsHandler {

    @ExceptionHandler(UserServiceException.class)
    public ResponseEntity<ErrorResponse> handle(UserServiceException ex) {
        BadRequestException exception = new BadRequestException(ex.getMessage());
        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }

}
