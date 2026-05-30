package com.chamber.ecommerce.catalogo.application;

import com.chamber.ecommerce.catalogo.domain.model.gateway.ProductoGateway;
import com.chamber.ecommerce.catalogo.domain.model.usecase.ProductoUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class useCaseConfig {
    @Bean
    public ProductoUseCase productoUseCase(ProductoGateway productoGateway) {
        return new ProductoUseCase(productoGateway);
    }
}
