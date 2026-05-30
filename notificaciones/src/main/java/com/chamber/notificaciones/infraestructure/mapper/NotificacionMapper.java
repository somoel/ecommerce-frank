package com.chamber.notificaciones.infraestructure.mapper;

import com.chamber.notificaciones.domain.model.Notificacion;
import com.chamber.notificaciones.infraestructure.driver_adapters.jpa_repository.NotificacionData;
import org.springframework.stereotype.Component;

@Component
public class NotificacionMapper {

    public NotificacionData toData(Notificacion n) {
        return new NotificacionData(
                n.getId(),
                n.getDestinatarioEmail(),
                n.getTipo(),
                n.getAsunto(),
                n.getMensaje(),
                n.getEnviada(),
                n.getFechaEnvio()
        );
    }

    public Notificacion toDomain(NotificacionData d) {
        return new Notificacion(
                d.getId(),
                d.getDestinatarioEmail(),
                d.getTipo(),
                d.getAsunto(),
                d.getMensaje(),
                d.getEnviada(),
                d.getFechaEnvio()
        );
    }
}