package com.chamber.ecommerce.auth.infraestructure.entry_points.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String email;
    private String cedula;
    private String rol;
}
