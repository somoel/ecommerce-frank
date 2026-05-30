package com.chamber.ecommerce.ordenes.domain.model.exception;

public class EstadoOrdenInvalidoException extends RuntimeException {
    public EstadoOrdenInvalidoException(String message) {
        super(message);
    }
}