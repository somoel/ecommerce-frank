package com.chamber.ecommerce.ordenes.infraestructure.entry_points;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemOrdenRequest {
    private String productoId;
    private Integer cantidad;
    private Double precioUnitario;
}