package com.chamber.ecommerce.auth.infraestructure.security;

import com.chamber.ecommerce.auth.domain.model.Usuario;
import com.chamber.ecommerce.auth.domain.model.gateway.TokenGateway;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenGatewayImpl implements TokenGateway {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String generateToken(Usuario usuario) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(usuario.getEmail())
                .claim("cedula", usuario.getCedula())
                .claim("rol", usuario.getRol())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(getSigningKey())
                .compact();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }
}
