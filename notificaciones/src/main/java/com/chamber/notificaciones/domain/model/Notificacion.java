package com.chamber.notificaciones.domain.model;

import lombok.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Notificacion {
    private Long id;
    private String destinatarioEmail;
    private String tipo;           // ORDEN_CREADA, ORDEN_PAGADA, ORDEN_ENVIADA, ORDEN_CANCELADA, BIENVENIDA
    private String asunto;
    private String mensaje;
    private Boolean enviada;
    private LocalDateTime fechaEnvio;
}