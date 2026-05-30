package com.chamber.inventario.domain.model.usecase;

import com.chamber.inventario.domain.model.Inventario;
import com.chamber.inventario.domain.model.exception.DatosInventarioInvalidosException;
import com.chamber.inventario.domain.model.exception.ProductoInventarioNoEncontradoException;
import com.chamber.inventario.domain.model.exception.StockInsuficienteException;
import com.chamber.inventario.domain.model.gateway.InventarioGateway;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class InventarioUseCase {

    private final InventarioGateway inventarioGateway;

    public Inventario registrarProducto(Inventario inventario) {
        if (inventario.getProductoId() == null || inventario.getProductoId().isBlank()) {
            throw new DatosInventarioInvalidosException("El ID del producto es obligatorio");
        }
        if (inventario.getNombreProducto() == null || inventario.getNombreProducto().isBlank()) {
            throw new DatosInventarioInvalidosException("El nombre del producto es obligatorio");
        }
        if (inventario.getStockActual() == null || inventario.getStockActual() < 0) {
            throw new DatosInventarioInvalidosException("El stock actual debe ser mayor o igual a 0");
        }
        if (inventario.getStockMinimo() == null || inventario.getStockMinimo() < 0) {
            throw new DatosInventarioInvalidosException("El stock mínimo debe ser mayor o igual a 0");
        }

        inventario.setAlertaStockBajo(inventario.getStockActual() <= inventario.getStockMinimo());
        return inventarioGateway.guardar(inventario);
    }

    public Inventario buscarPorProductoId(String productoId) {
        Inventario inventario = inventarioGateway.buscarPorProductoId(productoId);
        if (inventario == null) {
            throw new ProductoInventarioNoEncontradoException("Producto no encontrado en inventario: " + productoId);
        }
        return inventario;
    }

    public List<Inventario> listarTodos() {
        return inventarioGateway.listarTodos();
    }

    public List<Inventario> listarConAlertaStockBajo() {
        return inventarioGateway.listarConAlertaStockBajo();
    }

    // Aumentar stock (ej: cuando llega mercancía)
    public Inventario aumentarStock(String productoId, Integer cantidad) {
        if (cantidad == null || cantidad <= 0) {
            throw new DatosInventarioInvalidosException("La cantidad a aumentar debe ser mayor a 0");
        }
        Inventario inventario = buscarPorProductoId(productoId);
        inventario.setStockActual(inventario.getStockActual() + cantidad);
        inventario.setAlertaStockBajo(inventario.getStockActual() <= inventario.getStockMinimo());
        return inventarioGateway.guardar(inventario);
    }

    // Reducir stock (ej: cuando se crea una orden)
    public Inventario reducirStock(String productoId, Integer cantidad) {
        if (cantidad == null || cantidad <= 0) {
            throw new DatosInventarioInvalidosException("La cantidad a reducir debe ser mayor a 0");
        }
        Inventario inventario = buscarPorProductoId(productoId);
        if (inventario.getStockActual() < cantidad) {
            throw new StockInsuficienteException(
                    "Stock insuficiente para el producto " + productoId +
                            ". Stock actual: " + inventario.getStockActual() +
                            ", cantidad solicitada: " + cantidad
            );
        }
        inventario.setStockActual(inventario.getStockActual() - cantidad);
        inventario.setAlertaStockBajo(inventario.getStockActual() <= inventario.getStockMinimo());
        return inventarioGateway.guardar(inventario);
    }

    public void eliminarProducto(String productoId) {
        if (inventarioGateway.buscarPorProductoId(productoId) == null) {
            throw new ProductoInventarioNoEncontradoException("Producto no encontrado en inventario: " + productoId);
        }
        inventarioGateway.eliminar(productoId);
    }
}