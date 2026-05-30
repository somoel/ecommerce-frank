package com.chamber.ecommerce.ordenes.application;

import com.chamber.ecommerce.ordenes.domain.model.gateway.OrdenGateway;
import com.chamber.ecommerce.ordenes.domain.model.usecase.OrdenUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class useCaseConfig {
    @Bean
    public OrdenUseCase ordenUseCase(OrdenGateway ordenGateway) {
        return new OrdenUseCase(ordenGateway);
    }
}