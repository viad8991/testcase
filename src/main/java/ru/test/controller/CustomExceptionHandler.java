package ru.test.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import ru.test.exeptions.PhoneNumberExceptions;

@RestControllerAdvice
public class CustomExceptionHandler {

    // -------- Bad Request 400 --------

    @ExceptionHandler({
            PhoneNumberExceptions.class
    })
    ResponseEntity<String> handleBadRequest(RuntimeException ex, WebRequest request) {
        return ResponseEntity
                .badRequest()
                .body(ex.getMessage());
    }

}
