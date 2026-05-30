package com.chamber.ecommerce.catalogo.domain.model.gateway;

import com.chamber.ecommerce.catalogo.domain.model.Producto;

public interface ProductoGateway {
    Producto guardarProducto(Producto producto);

    Producto buscarProducto(String id);

    void eliminarProducto(String id);
}
