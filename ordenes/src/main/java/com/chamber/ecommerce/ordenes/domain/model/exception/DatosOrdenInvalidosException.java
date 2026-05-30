package com.chamber.ecommerce.ordenes.domain.model.exception;

public class DatosOrdenInvalidosException extends RuntimeException {
    public DatosOrdenInvalidosException(String message) {
        super(message);
    }
}