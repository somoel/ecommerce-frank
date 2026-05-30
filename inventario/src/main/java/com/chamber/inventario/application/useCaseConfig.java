package com.chamber.inventario.application;

import com.chamber.inventario.domain.model.gateway.InventarioGateway;
import com.chamber.inventario.domain.model.usecase.InventarioUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class useCaseConfig {
    @Bean
    public InventarioUseCase inventarioUseCase(InventarioGateway inventarioGateway) {
        return new InventarioUseCase(inventarioGateway);
    }
}
