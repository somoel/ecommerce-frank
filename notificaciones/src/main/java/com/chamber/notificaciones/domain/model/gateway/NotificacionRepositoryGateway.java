package com.chamber.notificaciones.domain.model.gateway;

import com.chamber.notificaciones.domain.model.Notificacion;
import java.util.List;

public interface NotificacionRepositoryGateway {
    Notificacion guardar(Notificacion notificacion);
    List<Notificacion> buscarPorEmail(String email);
}