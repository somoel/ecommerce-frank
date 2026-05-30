package com.chamber.notificaciones.application;

import com.chamber.notificaciones.domain.model.gateway.NotificacionGateway;
import com.chamber.notificaciones.domain.model.gateway.NotificacionRepositoryGateway;
import com.chamber.notificaciones.domain.model.usecase.NotificacionUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class useCaseConfig {
    @Bean
    public NotificacionUseCase notificacionUseCase(
            NotificacionGateway notificacionGateway,
            NotificacionRepositoryGateway repositoryGateway
    ) {
        return new NotificacionUseCase(notificacionGateway, repositoryGateway);
    }
}