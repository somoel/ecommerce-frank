package com.chamber.ecommerce.auth.application;

import com.chamber.ecommerce.auth.domain.model.gateway.SecurityGateway;
import com.chamber.ecommerce.auth.domain.model.gateway.UsuarioGateway;
import com.chamber.ecommerce.auth.domain.model.usecase.UsuarioUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class useCaseConfig {
    @Bean
    public UsuarioUseCase usuarioUseCase(UsuarioGateway usuarioGateway, SecurityGateway securityGateway) {
        return new UsuarioUseCase(usuarioGateway, securityGateway);
    }
}
