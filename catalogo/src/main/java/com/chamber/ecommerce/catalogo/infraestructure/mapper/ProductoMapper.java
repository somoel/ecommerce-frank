package com.chamber.ecommerce.catalogo.infraestructure.mapper;

import com.chamber.ecommerce.catalogo.domain.model.Producto;
import com.chamber.ecommerce.catalogo.infraestructure.driver_adapters.jpa_repository.ProductoData;
import com.chamber.ecommerce.catalogo.infraestructure.entry_points.dto.ProductoRequest;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {
    public ProductoData toProductoData(Producto producto) {
        return new ProductoData(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock()
        );
    }

    public Producto toProducto(ProductoData productoData) {
        return new Producto(
                productoData.getId(),
                productoData.getNombre(),
                productoData.getDescripcion(),
                productoData.getPrecio(),
                productoData.getStock()
        );
    }

    public Producto toProducto(ProductoRequest productoRequest) {
        return new Producto(
                productoRequest.getId(),
                productoRequest.getNombre(),
                productoRequest.getDescripcion(),
                productoRequest.getPrecio(),
                productoRequest.getStock()
        );
    }
}
