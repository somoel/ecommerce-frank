package com.chamber.notificaciones.domain.model.gateway;

import com.chamber.notificaciones.domain.model.Notificacion;

public interface NotificacionGateway {
    void enviarEmail(Notificacion notificacion);
}