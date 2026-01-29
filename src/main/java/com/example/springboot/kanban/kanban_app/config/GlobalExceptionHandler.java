package com.example.springboot.kanban.kanban_app.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("El cuerpo de la petición es inválido o contiene valores incorrectos: " + ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("El argumento proporcionado es de un tipo inválido");
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<String> handleNoResourceFound(NoResourceFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("El recurso solicitado no fue encontrado");
    }

}