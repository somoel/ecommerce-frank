package com.chamber.inventario.infraestructure.driver_adapters.jpa_repository;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inventario")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class InventarioData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String productoId;

    @Column(nullable = false)
    private String nombreProducto;

    @Column(nullable = false)
    private Integer stockActual;

    @Column(nullable = false)
    private Integer stockMinimo;

    @Column(nullable = false)
    private Boolean alertaStockBajo;
}
