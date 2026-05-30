package com.chamber.ecommerce.catalogo.infraestructure.entry_points.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductoRequest {
    private String id;
    private String nombre;
    private String descripcion;
    private Object precio;
    private Integer stock;
}
