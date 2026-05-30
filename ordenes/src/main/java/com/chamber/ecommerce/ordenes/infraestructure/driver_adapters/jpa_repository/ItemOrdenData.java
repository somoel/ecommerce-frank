package com.chamber.ecommerce.ordenes.infraestructure.driver_adapters.jpa_repository;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "item_orden")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class ItemOrdenData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productoId;
    private Integer cantidad;
    private Double precioUnitario;
}