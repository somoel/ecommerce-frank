package com.chamber.notificaciones.domain.model.usecase;

import com.chamber.notificaciones.domain.model.Notificacion;
import com.chamber.notificaciones.domain.model.exception.NotificacionException;
import com.chamber.notificaciones.domain.model.gateway.NotificacionGateway;
import com.chamber.notificaciones.domain.model.gateway.NotificacionRepositoryGateway;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class NotificacionUseCase {

    private final NotificacionGateway notificacionGateway;
    private final NotificacionRepositoryGateway repositoryGateway;

    public Notificacion enviarNotificacion(Notificacion notificacion) {
        if (notificacion == null) {
            throw new NotificacionException("La notificación no puede ser nula");
        }
        if (notificacion.getDestinatarioEmail() == null || notificacion.getDestinatarioEmail().isBlank()) {
            throw new NotificacionException("El email del destinatario es obligatorio");
        }
        if (notificacion.getTipo() == null || notificacion.getTipo().isBlank()) {
            throw new NotificacionException("El tipo de notificación es obligatorio");
        }
        if (notificacion.getMensaje() == null || notificacion.getMensaje().isBlank()) {
            throw new NotificacionException("El mensaje no puede estar vacío");
        }

        notificacion.setFechaEnvio(LocalDateTime.now());

        try {
            notificacionGateway.enviarEmail(notificacion);
            notificacion.setEnviada(true);
        } catch (Exception e) {
            // Si falla el envío, lo guardamos igual con enviada=false para reintento
            notificacion.setEnviada(false);
        }

        return repositoryGateway.guardar(notificacion);
    }

    public List<Notificacion> buscarPorEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new NotificacionException("El email es obligatorio");
        }
        return repositoryGateway.buscarPorEmail(email);
    }
}