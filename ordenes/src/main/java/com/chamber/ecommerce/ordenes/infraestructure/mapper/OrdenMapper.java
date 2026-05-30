package com.chamber.ecommerce.ordenes.infraestructure.mapper;

import com.chamber.ecommerce.ordenes.domain.model.ItemOrden;
import com.chamber.ecommerce.ordenes.domain.model.Orden;
import com.chamber.ecommerce.ordenes.infraestructure.driver_adapters.jpa_repository.ItemOrdenData;
import com.chamber.ecommerce.ordenes.infraestructure.driver_adapters.jpa_repository.OrdenData;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrdenMapper {

    public OrdenData toOrdenData(Orden orden) {
        List<ItemOrdenData> items = orden.getItems().stream()
                .map(item -> new ItemOrdenData(null, item.getProductoId(), item.getCantidad(), item.getPrecioUnitario()))
                .collect(Collectors.toList());
        return new OrdenData(orden.getId(), orden.getCedulaUsuario(), items, orden.getTotal(), orden.getEstado(), orden.getFechaCreacion());
    }

    public Orden toOrden(OrdenData data) {
        List<ItemOrden> items = data.getItems().stream()
                .map(item -> new ItemOrden(item.getProductoId(), item.getCantidad(), item.getPrecioUnitario()))
                .collect(Collectors.toList());
        return new Orden(data.getId(), data.getCedulaUsuario(), items, data.getTotal(), data.getEstado(), data.getFechaCreacion());
    }
}