package com.chamber.notificaciones.infraestructure.entry_points.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificacionRequest {
    private String destinatarioEmail;
    private String tipo;
    private String asunto;
    private String mensaje;
}