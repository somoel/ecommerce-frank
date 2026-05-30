package com.chamber.ecommerce.auth.infraestructure.driver_adapters.jpa_repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "usuario")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class UsuarioData {
    @Id
    private String cedula;
    private String nombre;

    @Column(length = 10)
    private String telefono;

    @Column(length = 30, nullable = false)
    private String email;
    private String password;

    private Integer edad;
    private String rol;
}
