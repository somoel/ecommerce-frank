package com.chamber.ecommerce.ordenes.domain.model;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Orden {
    private String id;
    private String cedulaUsuario;
    private List<ItemOrden> items;
    private Double total;
    private String estado;
    private LocalDateTime fechaCreacion;
}