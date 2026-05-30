package com.chamber.ecommerce.ordenes.infraestructure.driver_adapters.jpa_repository;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orden")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class OrdenData {
    @Id
    private String id;

    @Column(nullable = false)
    private String cedulaUsuario;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "orden_id")
    private List<ItemOrdenData> items;

    @Column(nullable = false)
    private Double total;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;
}