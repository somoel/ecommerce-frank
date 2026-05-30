package com.chamber.ecommerce.ordenes.domain.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ItemOrden {
    private String productoId;
    private Integer cantidad;
    private Double precioUnitario;
}