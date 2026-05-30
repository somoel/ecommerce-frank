package com.chamber.inventario.infraestructure.mapper;

import com.chamber.inventario.domain.model.Inventario;
import com.chamber.inventario.infraestructure.driver_adapters.jpa_repository.InventarioData;
import org.springframework.stereotype.Component;

@Component
public class InventarioMapper {

    public InventarioData toData(Inventario i) {
        return new InventarioData(
                i.getId(),
                i.getProductoId(),
                i.getNombreProducto(),
                i.getStockActual(),
                i.getStockMinimo(),
                i.getAlertaStockBajo()
        );
    }

    public Inventario toDomain(InventarioData d) {
        return new Inventario(
                d.getId(),
                d.getProductoId(),
                d.getNombreProducto(),
                d.getStockActual(),
                d.getStockMinimo(),
                d.getAlertaStockBajo()
        );
    }
}

