package com.chamber.ecommerce.ordenes.domain.model.usecase;

import com.chamber.ecommerce.ordenes.domain.model.Orden;
import com.chamber.ecommerce.ordenes.domain.model.exception.DatosOrdenInvalidosException;
import com.chamber.ecommerce.ordenes.domain.model.exception.EstadoOrdenInvalidoException;
import com.chamber.ecommerce.ordenes.domain.model.exception.OrdenNoEncontradaException;
import com.chamber.ecommerce.ordenes.domain.model.gateway.OrdenGateway;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
public class OrdenUseCase {

    private static final Set<String> ESTADOS_VALIDOS = Set.of("PENDIENTE", "PAGADA", "CANCELADA", "ENVIADA");

    private final OrdenGateway ordenGateway;

    public Orden crearOrden(Orden orden) {
        if (orden == null) {
            throw new DatosOrdenInvalidosException("La orden no puede ser nula");
        }
        if (orden.getCedulaUsuario() == null || orden.getCedulaUsuario().isBlank()) {
            throw new DatosOrdenInvalidosException("La cédula del usuario es obligatoria");
        }
        if (orden.getItems() == null || orden.getItems().isEmpty()) {
            throw new DatosOrdenInvalidosException("La orden debe tener al menos un item");
        }

        orden.setId(UUID.randomUUID().toString());
        orden.setEstado("PENDIENTE");
        orden.setFechaCreacion(LocalDateTime.now());

        double total = orden.getItems().stream()
                .mapToDouble(item -> item.getPrecioUnitario() * item.getCantidad())
                .sum();
        orden.setTotal(total);

        return ordenGateway.guardarOrden(orden);
    }

    public Orden buscarOrden(String id) {
        Orden orden = ordenGateway.buscarOrden(id);
        if (orden == null) {
            throw new OrdenNoEncontradaException("Orden no encontrada con id: " + id);
        }
        return orden;
    }

    public List<Orden> buscarPorUsuario(String cedulaUsuario) {
        if (cedulaUsuario == null || cedulaUsuario.isBlank()) {
            throw new DatosOrdenInvalidosException("La cédula del usuario es obligatoria");
        }
        return ordenGateway.buscarPorUsuario(cedulaUsuario);
    }

    public Orden actualizarEstado(String id, String nuevoEstado) {
        if (nuevoEstado == null || !ESTADOS_VALIDOS.contains(nuevoEstado.toUpperCase())) {
            throw new EstadoOrdenInvalidoException("Estado inválido: " + nuevoEstado + ". Válidos: " + ESTADOS_VALIDOS);
        }
        Orden orden = ordenGateway.buscarOrden(id);
        if (orden == null) {
            throw new OrdenNoEncontradaException("Orden no encontrada con id: " + id);
        }
        orden.setEstado(nuevoEstado.toUpperCase());
        return ordenGateway.guardarOrden(orden);
    }

    public void eliminarOrden(String id) {
        if (ordenGateway.buscarOrden(id) == null) {
            throw new OrdenNoEncontradaException("Orden no encontrada con id: " + id);
        }
        ordenGateway.eliminarOrden(id);
    }
}