package com.chamber.inventario.domain.model.exception;

public class ProductoInventarioNoEncontradoException extends RuntimeException {
    public ProductoInventarioNoEncontradoException(String message) {
        super(message);
    }
}