package com.chamber.inventario.infraestructure.entry_points.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventarioRequest {
    private String productoId;
    private String nombreProducto;
    private Integer stockActual;
    private Integer stockMinimo;
}

