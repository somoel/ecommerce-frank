package com.chamber.notificaciones.infraestructure.driver_adapters.jpa_repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificacionDataJpaRepository extends JpaRepository<NotificacionData, Long> {
    List<NotificacionData> findByDestinatarioEmail(String destinatarioEmail);
}