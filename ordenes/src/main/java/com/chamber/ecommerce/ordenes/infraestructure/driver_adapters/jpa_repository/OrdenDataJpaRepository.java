package com.chamber.ecommerce.ordenes.infraestructure.driver_adapters.jpa_repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrdenDataJpaRepository extends JpaRepository<OrdenData, String> {
    List<OrdenData> findByCedulaUsuario(String cedulaUsuario);
}
