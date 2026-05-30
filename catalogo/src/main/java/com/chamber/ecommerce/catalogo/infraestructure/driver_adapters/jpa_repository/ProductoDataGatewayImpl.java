package com.chamber.ecommerce.catalogo.infraestructure.driver_adapters.jpa_repository;

import com.chamber.ecommerce.catalogo.domain.model.Producto;
import com.chamber.ecommerce.catalogo.domain.model.gateway.ProductoGateway;
import com.chamber.ecommerce.catalogo.infraestructure.mapper.ProductoMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductoDataGatewayImpl implements ProductoGateway {
    private final ProductoDataJpaRepository repository;
    private final ProductoMapper productoMapper;

    @Override
    public List<Producto> listarProductos() {
        return repository.findAll().stream()
                .map(productoMapper::toProducto)
                .toList();
    }

    @Override
    public Producto guardarProducto(Producto producto) {
        ProductoData productoData = productoMapper.toProductoData(producto);
        ProductoData productoGuardadoData = repository.save(productoData);
        return productoMapper.toProducto(productoGuardadoData);
    }

    @Override
    public Producto buscarProducto(String id) {
        return repository.findById(id).map(productoMapper::toProducto).orElse(null);
    }

    @Override
    public Producto actualizarProducto(String id, Producto producto) {
        ProductoData existente = repository.findById(id).orElse(null);
        if (existente == null) {
            return null;
        }
        if (producto.getNombre() != null) {
            existente.setNombre(producto.getNombre());
        }
        if (producto.getDescripcion() != null) {
            existente.setDescripcion(producto.getDescripcion());
        }
        if (producto.getPrecio() != null) {
            existente.setPrecio(producto.getPrecio());
        }
        ProductoData guardado = repository.save(existente);
        return productoMapper.toProducto(guardado);
    }

    @Override
    public void eliminarProducto(String id) {
        repository.deleteById(id);
    }
}
