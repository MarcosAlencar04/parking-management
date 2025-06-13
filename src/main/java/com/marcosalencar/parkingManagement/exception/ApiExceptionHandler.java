package com.marcosalencar.parkingManagement.exception;

import java.util.Map;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String,String>> handleBadRequest(BadRequestException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,Object>> handleGeneric(Exception ex, HttpServletRequest req) {
        String path = req.getRequestURI();
        String message;
        ex.printStackTrace();
        switch (path) {
            case "/webhook":
                message = "Erro ao controlar fluxo do estacionamento";
                break;
            case "/plate-status":
                message = "Erro ao buscar informações da placa";
                break;
            case "/garage":
                message = "Erro ao buscar informações da garagem";
                break;
            case "/spot-status":
                message = "Erro ao buscar informações da vaga";
                break;
            default:
                message = "Erro interno no servidor";
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "message", message,
                    "error", ex.getMessage()
                ));
    }
}

