package com.chamber.inventario.infraestructure.entry_points.handler;

import com.chamber.inventario.domain.model.exception.DatosInventarioInvalidosException;
import com.chamber.inventario.domain.model.exception.ProductoInventarioNoEncontradoException;
import com.chamber.inventario.domain.model.exception.StockInsuficienteException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class UseCaseExceptionHandler {

    @ExceptionHandler(ProductoInventarioNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleNoEncontrado(
            ProductoInventarioNoEncontradoException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(StockInsuficienteException.class)
    public ResponseEntity<ErrorResponse> handleStockInsuficiente(
            StockInsuficienteException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    @ExceptionHandler(DatosInventarioInvalidosException.class)
    public ResponseEntity<ErrorResponse> handleDatosInvalidos(
            DatosInventarioInvalidosException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(
            RuntimeException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message, HttpServletRequest request) {
        return ResponseEntity.status(status).body(
                new ErrorResponse(LocalDateTime.now(), status.value(), status.getReasonPhrase(), message, request.getRequestURI())
        );
    }
}
