package com.chamber.ecommerce.ordenes.infraestructure.entry_points;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class OrdenRequest {
    private String cedulaUsuario;
    private List<ItemOrdenRequest> items;
}