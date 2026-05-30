package com.chamber.ecommerce.catalogo.domain.model.gateway;

import com.chamber.ecommerce.catalogo.domain.model.Producto;
import java.util.List;

public interface ProductoGateway {
    Producto guardarProducto(Producto producto);

    Producto buscarProducto(String id);

    Producto actualizarProducto(String id, Producto producto);

    void eliminarProducto(String id);

    List<Producto> listarProductos();
}
