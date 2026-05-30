package com.chamber.ecommerce.auth.infraestructure.entry_points.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String email;
    private String password;
}
