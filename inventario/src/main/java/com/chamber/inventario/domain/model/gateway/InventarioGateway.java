package com.chamber.inventario.domain.model.gateway;

import com.chamber.inventario.domain.model.Inventario;
import java.util.List;

public interface InventarioGateway {
    Inventario guardar(Inventario inventario);
    Inventario buscarPorProductoId(String productoId);
    List<Inventario> listarTodos();
    List<Inventario> listarConAlertaStockBajo();
    void eliminar(String productoId);
}