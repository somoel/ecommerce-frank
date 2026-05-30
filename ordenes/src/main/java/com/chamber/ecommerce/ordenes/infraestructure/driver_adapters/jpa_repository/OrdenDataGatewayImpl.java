package com.chamber.ecommerce.ordenes.infraestructure.driver_adapters.jpa_repository;

import com.chamber.ecommerce.ordenes.domain.model.Orden;
import com.chamber.ecommerce.ordenes.domain.model.gateway.OrdenGateway;
import com.chamber.ecommerce.ordenes.infraestructure.mapper.OrdenMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrdenDataGatewayImpl implements OrdenGateway {
    private final OrdenDataJpaRepository repository;
    private final OrdenMapper ordenMapper;

    @Override
    public Orden guardarOrden(Orden orden) {
        OrdenData data = ordenMapper.toOrdenData(orden);
        OrdenData saved = repository.save(data);
        return ordenMapper.toOrden(saved);
    }

    @Override
    public Orden buscarOrden(String id) {
        return repository.findById(id).map(ordenMapper::toOrden).orElse(null);
    }

    @Override
    public List<Orden> buscarPorUsuario(String cedulaUsuario) {
        return repository.findByCedulaUsuario(cedulaUsuario)
                .stream()
                .map(ordenMapper::toOrden)
                .collect(Collectors.toList());
    }

    @Override
    public void eliminarOrden(String id) {
        repository.deleteById(id);
    }
}