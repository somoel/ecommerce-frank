package com.chamber.ecommerce.ordenes.domain.model.exception;

public class OrdenNoEncontradaException extends RuntimeException {
    public OrdenNoEncontradaException(String message) {
        super(message);
    }
}