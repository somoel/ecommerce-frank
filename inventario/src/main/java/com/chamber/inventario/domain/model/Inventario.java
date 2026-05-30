package com.chamber.inventario.domain.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Inventario {
    private Long id;
    private String productoId;      // referencia al id del microservicio catalogo
    private String nombreProducto;
    private Integer stockActual;
    private Integer stockMinimo;    // umbral para alerta de stock bajo
    private Boolean alertaStockBajo; // true cuando stockActual <= stockMinimo
}