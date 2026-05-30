package com.chamber.ecommerce.auth.domain.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Usuario {

    private String cedula;
    private String nombre;
    private String telefono;
    private String email;
    private String password;
    private Integer edad;
    private String rol;

}