package com.chamber.ecommerce.auth.domain.model.exception;

public class DatosInvalidosException extends RuntimeException {
    public DatosInvalidosException(String message) {
        super(message);
    }
}
