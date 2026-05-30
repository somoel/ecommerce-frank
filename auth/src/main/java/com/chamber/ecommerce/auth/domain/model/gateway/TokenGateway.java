package com.chamber.ecommerce.auth.domain.model.gateway;

import com.chamber.ecommerce.auth.domain.model.Usuario;

public interface TokenGateway {
    String generateToken(Usuario usuario);
    boolean validateToken(String token);
    String getEmailFromToken(String token);
}
