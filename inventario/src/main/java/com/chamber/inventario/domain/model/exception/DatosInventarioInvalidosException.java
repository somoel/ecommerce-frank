package com.chamber.inventario.domain.model.exception;

public class DatosInventarioInvalidosException extends RuntimeException {
    public DatosInventarioInvalidosException(String message) {
        super(message);
    }
}