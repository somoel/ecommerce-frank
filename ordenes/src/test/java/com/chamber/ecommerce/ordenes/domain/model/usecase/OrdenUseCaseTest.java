package com.chamber.ecommerce.ordenes.domain.model.usecase;

import com.chamber.ecommerce.ordenes.domain.model.ItemOrden;
import com.chamber.ecommerce.ordenes.domain.model.Orden;
import com.chamber.ecommerce.ordenes.domain.model.exception.DatosOrdenInvalidosException;
import com.chamber.ecommerce.ordenes.domain.model.exception.EstadoOrdenInvalidoException;
import com.chamber.ecommerce.ordenes.domain.model.exception.OrdenNoEncontradaException;
import com.chamber.ecommerce.ordenes.domain.model.gateway.OrdenGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class OrdenUseCaseTest {

    @Mock
    private OrdenGateway ordenGateway;

    @InjectMocks
    private OrdenUseCase ordenUseCase;

    @Test
    void crearOrdenGeneraUuidEstadoPendienteYCalculeTotal() {
        FakeOrdenGateway ordenGateway = new FakeOrdenGateway();
        OrdenUseCase ordenUseCase = new OrdenUseCase(ordenGateway);
        Orden orden = crearOrden("1234567890", List.of(
                crearItem("prod-1", 2, 10.0),
                crearItem("prod-2", 1, 25.5)
        ));

        Orden resultado = ordenUseCase.crearOrden(orden);

        assertNotNull(resultado.getId());
        assertNotEquals(null, resultado.getId());
        assertEquals("PENDIENTE", resultado.getEstado());
        assertNotNull(resultado.getFechaCreacion());
        assertEquals(45.5, resultado.getTotal(), 0.01);
        assertNotNull(ordenGateway.ordenGuardada);
    }

    @Test
    void crearOrdenFallaCuandoOrdenEsNula() {
        OrdenUseCase ordenUseCase = new OrdenUseCase(new FakeOrdenGateway());

        DatosOrdenInvalidosException error = assertThrows(
                DatosOrdenInvalidosException.class,
                () -> ordenUseCase.crearOrden(null)
        );

        assertEquals("La orden no puede ser nula", error.getMessage());
    }

    @Test
    void crearOrdenFallaCuandoCedulaUsuarioEsNulaOVacia() {
        OrdenUseCase ordenUseCase = new OrdenUseCase(new FakeOrdenGateway());

        assertThrows(DatosOrdenInvalidosException.class, () -> ordenUseCase.crearOrden(
                crearOrden(null, List.of(crearItem("prod-1", 1, 10.0)))
        ));
        assertThrows(DatosOrdenInvalidosException.class, () -> ordenUseCase.crearOrden(
                crearOrden(" ", List.of(crearItem("prod-1", 1, 10.0)))
        ));
        assertThrows(DatosOrdenInvalidosException.class, () -> ordenUseCase.crearOrden(
                crearOrden("", List.of(crearItem("prod-1", 1, 10.0)))
        ));
    }

    @Test
    void crearOrdenFallaCuandoItemsSonNulosOVacios() {
        OrdenUseCase ordenUseCase = new OrdenUseCase(new FakeOrdenGateway());

        assertThrows(DatosOrdenInvalidosException.class, () -> ordenUseCase.crearOrden(
                crearOrden("1234567890", null)
        ));
        assertThrows(DatosOrdenInvalidosException.class, () -> ordenUseCase.crearOrden(
                crearOrden("1234567890", List.of())
        ));
    }

    @Test
    void crearOrdenCalculaTotalCorrectamenteConMultiplesItems() {
        FakeOrdenGateway ordenGateway = new FakeOrdenGateway();
        OrdenUseCase ordenUseCase = new OrdenUseCase(ordenGateway);
        Orden orden = crearOrden("1234567890", List.of(
                crearItem("prod-1", 3, 20.0),
                crearItem("prod-2", 2, 15.0),
                crearItem("prod-3", 1, 100.0)
        ));

        Orden resultado = ordenUseCase.crearOrden(orden);

        assertEquals(190.0, resultado.getTotal(), 0.01);
    }

    @Test
    void buscarOrdenRetornaOrdenExistente() {
        FakeOrdenGateway ordenGateway = new FakeOrdenGateway();
        Orden ordenExistente = crearOrden("1234567890", List.of(crearItem("prod-1", 1, 10.0)));
        ordenExistente.setId("orden-1");
        ordenGateway.ordenPorId = ordenExistente;
        OrdenUseCase ordenUseCase = new OrdenUseCase(ordenGateway);

        Orden resultado = ordenUseCase.buscarOrden("orden-1");

        assertEquals("orden-1", resultado.getId());
        assertEquals("1234567890", resultado.getCedulaUsuario());
    }

    @Test
    void buscarOrdenFallaCuandoNoExiste() {
        OrdenUseCase ordenUseCase = new OrdenUseCase(new FakeOrdenGateway());

        OrdenNoEncontradaException error = assertThrows(
                OrdenNoEncontradaException.class,
                () -> ordenUseCase.buscarOrden("id-inexistente")
        );

        assertEquals("Orden no encontrada con id: id-inexistente", error.getMessage());
    }

    @Test
    void buscarPorUsuarioRetornaListaDeOrdenes() {
        FakeOrdenGateway ordenGateway = new FakeOrdenGateway();
        Orden orden1 = crearOrden("1234567890", List.of(crearItem("prod-1", 1, 10.0)));
        orden1.setId("orden-1");
        Orden orden2 = crearOrden("1234567890", List.of(crearItem("prod-2", 2, 20.0)));
        orden2.setId("orden-2");
        ordenGateway.ordenesPorUsuario.put("1234567890", List.of(orden1, orden2));
        OrdenUseCase ordenUseCase = new OrdenUseCase(ordenGateway);

        List<Orden> resultado = ordenUseCase.buscarPorUsuario("1234567890");

        assertEquals(2, resultado.size());
        assertEquals("orden-1", resultado.get(0).getId());
        assertEquals("orden-2", resultado.get(1).getId());
    }

    @Test
    void buscarPorUsuarioFallaCuandoCedulaEsNulaOVacia() {
        OrdenUseCase ordenUseCase = new OrdenUseCase(new FakeOrdenGateway());

        assertThrows(DatosOrdenInvalidosException.class, () -> ordenUseCase.buscarPorUsuario(null));
        assertThrows(DatosOrdenInvalidosException.class, () -> ordenUseCase.buscarPorUsuario(" "));
        assertThrows(DatosOrdenInvalidosException.class, () -> ordenUseCase.buscarPorUsuario(""));
    }

    @Test
    void actualizarEstadoCambiaEstadoCorrectamente() {
        FakeOrdenGateway ordenGateway = new FakeOrdenGateway();
        Orden ordenExistente = crearOrden("1234567890", List.of(crearItem("prod-1", 1, 10.0)));
        ordenExistente.setId("orden-1");
        ordenExistente.setEstado("PENDIENTE");
        ordenGateway.ordenPorId = ordenExistente;
        OrdenUseCase ordenUseCase = new OrdenUseCase(ordenGateway);

        Orden resultado = ordenUseCase.actualizarEstado("orden-1", "PAGADA");

        assertEquals("PAGADA", resultado.getEstado());
        assertNotNull(ordenGateway.ordenGuardada);
    }

    @Test
    void actualizarEstadoConvierteAEstadoEnMayusculas() {
        FakeOrdenGateway ordenGateway = new FakeOrdenGateway();
        Orden ordenExistente = crearOrden("1234567890", List.of(crearItem("prod-1", 1, 10.0)));
        ordenExistente.setId("orden-1");
        ordenExistente.setEstado("PENDIENTE");
        ordenGateway.ordenPorId = ordenExistente;
        OrdenUseCase ordenUseCase = new OrdenUseCase(ordenGateway);

        Orden resultado = ordenUseCase.actualizarEstado("orden-1", "pagada");

        assertEquals("PAGADA", resultado.getEstado());
    }

    @Test
    void actualizarEstadoFallaConEstadoNuloOInvalido() {
        FakeOrdenGateway ordenGateway = new FakeOrdenGateway();
        Orden ordenExistente = crearOrden("1234567890", List.of(crearItem("prod-1", 1, 10.0)));
        ordenExistente.setId("orden-1");
        ordenGateway.ordenPorId = ordenExistente;
        OrdenUseCase ordenUseCase = new OrdenUseCase(ordenGateway);

        assertThrows(EstadoOrdenInvalidoException.class, () -> ordenUseCase.actualizarEstado("orden-1", null));
        assertThrows(EstadoOrdenInvalidoException.class, () -> ordenUseCase.actualizarEstado("orden-1", "EN_TRANSITO"));
        assertThrows(EstadoOrdenInvalidoException.class, () -> ordenUseCase.actualizarEstado("orden-1", "entregada"));
    }

    @Test
    void actualizarEstadoFallaCuandoOrdenNoExiste() {
        OrdenUseCase ordenUseCase = new OrdenUseCase(new FakeOrdenGateway());

        OrdenNoEncontradaException error = assertThrows(
                OrdenNoEncontradaException.class,
                () -> ordenUseCase.actualizarEstado("id-inexistente", "PAGADA")
        );

        assertEquals("Orden no encontrada con id: id-inexistente", error.getMessage());
    }

    @Test
    void actualizarEstadoAceptaTodosLosEstadosValidos() {
        String[] estadosValidos = {"PENDIENTE", "PAGADA", "CANCELADA", "ENVIADA"};

        for (String estado : estadosValidos) {
            FakeOrdenGateway ordenGateway = new FakeOrdenGateway();
            Orden ordenExistente = crearOrden("1234567890", List.of(crearItem("prod-1", 1, 10.0)));
            ordenExistente.setId("orden-" + estado);
            ordenExistente.setEstado("PENDIENTE");
            ordenGateway.ordenPorId = ordenExistente;
            OrdenUseCase ordenUseCase = new OrdenUseCase(ordenGateway);

            Orden resultado = ordenUseCase.actualizarEstado("orden-" + estado, estado);

            assertEquals(estado, resultado.getEstado());
        }
    }

    @Test
    void eliminarOrdenEliminaCuandoExiste() {
        FakeOrdenGateway ordenGateway = new FakeOrdenGateway();
        Orden ordenExistente = crearOrden("1234567890", List.of(crearItem("prod-1", 1, 10.0)));
        ordenExistente.setId("orden-1");
        ordenGateway.ordenPorId = ordenExistente;
        OrdenUseCase ordenUseCase = new OrdenUseCase(ordenGateway);

        ordenUseCase.eliminarOrden("orden-1");

        assertEquals("orden-1", ordenGateway.idEliminada);
    }

    @Test
    void eliminarOrdenFallaCuandoNoExiste() {
        OrdenUseCase ordenUseCase = new OrdenUseCase(new FakeOrdenGateway());

        OrdenNoEncontradaException error = assertThrows(
                OrdenNoEncontradaException.class,
                () -> ordenUseCase.eliminarOrden("id-inexistente")
        );

        assertEquals("Orden no encontrada con id: id-inexistente", error.getMessage());
    }

    private Orden crearOrden(String cedulaUsuario, List<ItemOrden> items) {
        return new Orden(
                null,
                cedulaUsuario,
                items,
                null,
                null,
                null
        );
    }

    private ItemOrden crearItem(String productoId, Integer cantidad, Double precioUnitario) {
        return new ItemOrden(productoId, cantidad, precioUnitario);
    }

    private static class FakeOrdenGateway implements OrdenGateway {
        private Orden ordenGuardada;
        private Orden ordenPorId;
        private String idEliminada;
        private final Map<String, List<Orden>> ordenesPorUsuario = new HashMap<>();

        @Override
        public Orden guardarOrden(Orden orden) {
            ordenGuardada = orden;
            return orden;
        }

        @Override
        public Orden buscarOrden(String id) {
            if (ordenPorId == null || !ordenPorId.getId().equals(id)) {
                return null;
            }
            return ordenPorId;
        }

        @Override
        public List<Orden> buscarPorUsuario(String cedulaUsuario) {
            return ordenesPorUsuario.getOrDefault(cedulaUsuario, new ArrayList<>());
        }

        @Override
        public void eliminarOrden(String id) {
            idEliminada = id;
        }
    }
}
