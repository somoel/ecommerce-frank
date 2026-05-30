package com.chamber.ecommerce.catalogo.domain.model.usecase;

import com.chamber.ecommerce.catalogo.domain.model.Producto;
import com.chamber.ecommerce.catalogo.domain.model.gateway.ProductoGateway;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductoUseCase {
    private final ProductoGateway productoGateway;

    public Producto guardarProducto(Producto producto) {
        if (producto.getNombre() == null || producto.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacio");
        }
        if (producto.getPrecio() == null || producto.getPrecio() < 0) {
            throw new IllegalArgumentException("El precio debe ser mayor o igual a 0");
        }
        if (producto.getStock() == null || producto.getStock() < 0) {
            throw new IllegalArgumentException("El stock debe ser mayor o igual a 0");
        }
        return productoGateway.guardarProducto(producto);
    }

    public Producto buscarProducto(String id) {
        try {
            return productoGateway.buscarProducto(id);
        } catch (RuntimeException e) {
            System.out.println("Producto no encontrado");
            return new Producto();
        }
    }

    public void eliminarProducto(String id) {
        try {
            productoGateway.eliminarProducto(id);
        } catch (RuntimeException e) {
            System.out.println("Producto no encontrado");
        }
    }
}
