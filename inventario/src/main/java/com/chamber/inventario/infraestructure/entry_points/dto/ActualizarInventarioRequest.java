package com.chamber.inventario.infraestructure.entry_points.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActualizarInventarioRequest {
    private String nombreProducto;
    private Integer stockMinimo;
}
