package com.chamber.ecommerce.auth.domain.model.gateway;

public interface SecurityGateway {
    String encrypt(String rawValue);

    boolean matches(String rawValue, String encryptedValue);
}
