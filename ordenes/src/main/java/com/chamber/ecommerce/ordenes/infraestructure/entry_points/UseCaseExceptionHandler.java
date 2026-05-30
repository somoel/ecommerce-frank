package com.chamber.ecommerce.ordenes.infraestructure.entry_points;

import com.chamber.ecommerce.ordenes.domain.model.exception.DatosOrdenInvalidosException;
import com.chamber.ecommerce.ordenes.domain.model.exception.EstadoOrdenInvalidoException;
import com.chamber.ecommerce.ordenes.domain.model.exception.OrdenNoEncontradaException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class UseCaseExceptionHandler {

    @ExceptionHandler(OrdenNoEncontradaException.class)
    public ResponseEntity<ErrorResponse> handleOrdenNoEncontrada(
            OrdenNoEncontradaException exception, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, exception.getMessage(), request);
    }

    @ExceptionHandler({DatosOrdenInvalidosException.class, EstadoOrdenInvalidoException.class})
    public ResponseEntity<ErrorResponse> handleDatosInvalidos(
            RuntimeException exception, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), request);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(
            RuntimeException exception, HttpServletRequest request) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), request);
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message, HttpServletRequest request) {
        return ResponseEntity.status(status).body(
                new ErrorResponse(LocalDateTime.now(), status.value(), status.getReasonPhrase(), message, request.getRequestURI())
        );
    }
}

