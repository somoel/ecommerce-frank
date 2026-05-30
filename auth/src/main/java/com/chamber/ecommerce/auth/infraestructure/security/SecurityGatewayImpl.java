package com.chamber.ecommerce.auth.infraestructure.security;

import com.chamber.ecommerce.auth.domain.model.gateway.SecurityGateway;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class SecurityGatewayImpl implements SecurityGateway {
    @Override
    public String encrypt(String rawValue) {
        return BCrypt.hashpw(rawValue, BCrypt.gensalt());
    }

    @Override
    public boolean matches(String rawValue, String encryptedValue) {
        return BCrypt.checkpw(rawValue, encryptedValue);
    }
}
