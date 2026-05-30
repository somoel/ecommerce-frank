package com.chamber.notificaciones.infraestructure.driver_adapters.jpa_repository;

import com.chamber.notificaciones.domain.model.Notificacion;
import com.chamber.notificaciones.domain.model.gateway.NotificacionRepositoryGateway;
import com.chamber.notificaciones.infraestructure.mapper.NotificacionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class NotificacionRepositoryGatewayImpl implements NotificacionRepositoryGateway {
    private final NotificacionDataJpaRepository repository;
    private final NotificacionMapper mapper;

    @Override
    public Notificacion guardar(Notificacion notificacion) {
        NotificacionData data = mapper.toData(notificacion);
        NotificacionData saved = repository.save(data);
        return mapper.toDomain(saved);
    }

    @Override
    public List<Notificacion> buscarPorEmail(String email) {
        return repository.findByDestinatarioEmail(email)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}