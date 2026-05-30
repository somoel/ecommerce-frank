package com.chamber.notificaciones.infraestructure.driver_adapters.jpa_repository;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notificacion")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class NotificacionData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String destinatarioEmail;

    @Column(nullable = false)
    private String tipo;

    @Column(nullable = false)
    private String asunto;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String mensaje;

    @Column(nullable = false)
    private Boolean enviada;

    @Column(nullable = false)
    private LocalDateTime fechaEnvio;
}