package com.chamber.inventario.infraestructure.driver_adapters.jpa_repository;

import com.chamber.inventario.domain.model.Inventario;
import com.chamber.inventario.domain.model.gateway.InventarioGateway;
import com.chamber.inventario.infraestructure.mapper.InventarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class InventarioDataGatewayImpl implements InventarioGateway {
    private final InventarioDataJpaRepository repository;
    private final InventarioMapper mapper;

    @Override
    public Inventario guardar(Inventario inventario) {
        InventarioData data = mapper.toData(inventario);
        return mapper.toDomain(repository.save(data));
    }

    @Override
    public Inventario buscarPorProductoId(String productoId) {
        return repository.findByProductoId(productoId)
                .map(mapper::toDomain)
                .orElse(null);
    }

    @Override
    public List<Inventario> listarTodos() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Inventario> listarConAlertaStockBajo() {
        return repository.findByAlertaStockBajoTrue().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void eliminar(String productoId) {
        repository.deleteByProductoId(productoId);
    }
}
