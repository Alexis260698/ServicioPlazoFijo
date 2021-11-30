package com.example.Proyecto.configuration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

public class ExceptionsHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ GeneralsExceptions.class })
    protected ResponseEntity<Object> GeneralException(
            Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, ((GeneralsExceptions) ex ).getBody(),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
