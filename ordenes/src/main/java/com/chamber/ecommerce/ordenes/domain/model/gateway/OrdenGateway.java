package com.chamber.ecommerce.ordenes.domain.model.gateway;

import com.chamber.ecommerce.ordenes.domain.model.Orden;
import java.util.List;

public interface OrdenGateway {
    Orden guardarOrden(Orden orden);
    Orden buscarOrden(String id);
    List<Orden> buscarPorUsuario(String cedulaUsuario);
    void eliminarOrden(String id);
}
