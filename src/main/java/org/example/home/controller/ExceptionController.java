package org.example.home.controller;

import org.example.home.exception.NotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.xml.bind.ValidationException;
import java.util.Optional;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> notFound(ValidationException e){
        return ResponseEntity.badRequest().body("{\n" +
                "\"message\" : \"" +
                e.getMessage() + "\"\n}");
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> accessError() {
        return ResponseEntity.of(Optional.of("Вам доступ запрещен, обратитесь к администратору!"));
    }
}
