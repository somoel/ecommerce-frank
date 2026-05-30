package com.chamber.inventario.infraestructure.driver_adapters.jpa_repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface InventarioDataJpaRepository extends JpaRepository<InventarioData, Long> {
    Optional<InventarioData> findByProductoId(String productoId);
    List<InventarioData> findByAlertaStockBajoTrue();
    void deleteByProductoId(String productoId);
}
