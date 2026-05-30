package com.chamber.inventario.domain.model.usecase;

import com.chamber.inventario.domain.model.Inventario;
import com.chamber.inventario.domain.model.exception.DatosInventarioInvalidosException;
import com.chamber.inventario.domain.model.exception.ProductoInventarioNoEncontradoException;
import com.chamber.inventario.domain.model.exception.StockInsuficienteException;
import com.chamber.inventario.domain.model.gateway.InventarioGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class InventarioUseCaseTest {

    @Mock
    private InventarioGateway inventarioGateway;

    @InjectMocks
    private InventarioUseCase inventarioUseCase;

    @Test
    void registrarProductoGuardaCorrectamente() {
        FakeInventarioGateway gateway = new FakeInventarioGateway();
        InventarioUseCase useCase = new InventarioUseCase(gateway);
        Inventario inventario = crearInventario("PROD-001", "Laptop", 50, 10);

        Inventario resultado = useCase.registrarProducto(inventario);

        assertEquals("PROD-001", resultado.getProductoId());
        assertEquals("Laptop", resultado.getNombreProducto());
        assertEquals(50, resultado.getStockActual());
        assertEquals(10, resultado.getStockMinimo());
        assertFalse(resultado.getAlertaStockBajo());
        assertNotNull(gateway.inventarioGuardado);
    }

    @Test
    void registrarProductoFallaCuandoProductoIdEsNulo() {
        InventarioUseCase useCase = new InventarioUseCase(new FakeInventarioGateway());

        DatosInventarioInvalidosException error = assertThrows(
                DatosInventarioInvalidosException.class,
                () -> useCase.registrarProducto(crearInventario(null, "Laptop", 50, 10))
        );

        assertEquals("El ID del producto es obligatorio", error.getMessage());
    }

    @Test
    void registrarProductoFallaCuandoProductoIdEstaEnBlanco() {
        InventarioUseCase useCase = new InventarioUseCase(new FakeInventarioGateway());

        DatosInventarioInvalidosException error = assertThrows(
                DatosInventarioInvalidosException.class,
                () -> useCase.registrarProducto(crearInventario(" ", "Laptop", 50, 10))
        );

        assertEquals("El ID del producto es obligatorio", error.getMessage());
    }

    @Test
    void registrarProductoFallaCuandoNombreProductoEsNulo() {
        InventarioUseCase useCase = new InventarioUseCase(new FakeInventarioGateway());

        DatosInventarioInvalidosException error = assertThrows(
                DatosInventarioInvalidosException.class,
                () -> useCase.registrarProducto(crearInventario("PROD-001", null, 50, 10))
        );

        assertEquals("El nombre del producto es obligatorio", error.getMessage());
    }

    @Test
    void registrarProductoFallaCuandoNombreProductoEstaEnBlanco() {
        InventarioUseCase useCase = new InventarioUseCase(new FakeInventarioGateway());

        DatosInventarioInvalidosException error = assertThrows(
                DatosInventarioInvalidosException.class,
                () -> useCase.registrarProducto(crearInventario("PROD-001", " ", 50, 10))
        );

        assertEquals("El nombre del producto es obligatorio", error.getMessage());
    }

    @Test
    void registrarProductoFallaCuandoStockActualEsNulo() {
        InventarioUseCase useCase = new InventarioUseCase(new FakeInventarioGateway());

        DatosInventarioInvalidosException error = assertThrows(
                DatosInventarioInvalidosException.class,
                () -> useCase.registrarProducto(crearInventario("PROD-001", "Laptop", null, 10))
        );

        assertEquals("El stock actual debe ser mayor o igual a 0", error.getMessage());
    }

    @Test
    void registrarProductoFallaCuandoStockActualEsNegativo() {
        InventarioUseCase useCase = new InventarioUseCase(new FakeInventarioGateway());

        DatosInventarioInvalidosException error = assertThrows(
                DatosInventarioInvalidosException.class,
                () -> useCase.registrarProducto(crearInventario("PROD-001", "Laptop", -5, 10))
        );

        assertEquals("El stock actual debe ser mayor o igual a 0", error.getMessage());
    }

    @Test
    void registrarProductoFallaCuandoStockMinimoEsNulo() {
        InventarioUseCase useCase = new InventarioUseCase(new FakeInventarioGateway());

        DatosInventarioInvalidosException error = assertThrows(
                DatosInventarioInvalidosException.class,
                () -> useCase.registrarProducto(crearInventario("PROD-001", "Laptop", 50, null))
        );

        assertEquals("El stock mínimo debe ser mayor o igual a 0", error.getMessage());
    }

    @Test
    void registrarProductoFallaCuandoStockMinimoEsNegativo() {
        InventarioUseCase useCase = new InventarioUseCase(new FakeInventarioGateway());

        DatosInventarioInvalidosException error = assertThrows(
                DatosInventarioInvalidosException.class,
                () -> useCase.registrarProducto(crearInventario("PROD-001", "Laptop", 50, -3))
        );

        assertEquals("El stock mínimo debe ser mayor o igual a 0", error.getMessage());
    }

    @Test
    void registrarProductoActivaAlertaStockBajoCuandoStockEsIgualAlMinimo() {
        FakeInventarioGateway gateway = new FakeInventarioGateway();
        InventarioUseCase useCase = new InventarioUseCase(gateway);
        Inventario inventario = crearInventario("PROD-001", "Laptop", 10, 10);

        Inventario resultado = useCase.registrarProducto(inventario);

        assertTrue(resultado.getAlertaStockBajo());
    }

    @Test
    void registrarProductoActivaAlertaStockBajoCuandoStockEsMenorAlMinimo() {
        FakeInventarioGateway gateway = new FakeInventarioGateway();
        InventarioUseCase useCase = new InventarioUseCase(gateway);
        Inventario inventario = crearInventario("PROD-001", "Laptop", 3, 10);

        Inventario resultado = useCase.registrarProducto(inventario);

        assertTrue(resultado.getAlertaStockBajo());
    }

    @Test
    void registrarProductoDesactivaAlertaStockBajoCuandoStockEsMayorAlMinimo() {
        FakeInventarioGateway gateway = new FakeInventarioGateway();
        InventarioUseCase useCase = new InventarioUseCase(gateway);
        Inventario inventario = crearInventario("PROD-001", "Laptop", 50, 10);

        Inventario resultado = useCase.registrarProducto(inventario);

        assertFalse(resultado.getAlertaStockBajo());
    }

    @Test
    void buscarPorProductoIdRetornaProductoExistente() {
        FakeInventarioGateway gateway = new FakeInventarioGateway();
        gateway.guardar(crearInventario("PROD-001", "Laptop", 50, 10));
        InventarioUseCase useCase = new InventarioUseCase(gateway);

        Inventario resultado = useCase.buscarPorProductoId("PROD-001");

        assertEquals("PROD-001", resultado.getProductoId());
        assertEquals("Laptop", resultado.getNombreProducto());
        assertEquals(50, resultado.getStockActual());
    }

    @Test
    void buscarPorProductoIdFallaCuandoNoExiste() {
        InventarioUseCase useCase = new InventarioUseCase(new FakeInventarioGateway());

        ProductoInventarioNoEncontradoException error = assertThrows(
                ProductoInventarioNoEncontradoException.class,
                () -> useCase.buscarPorProductoId("PROD-999")
        );

        assertEquals("Producto no encontrado en inventario: PROD-999", error.getMessage());
    }

    @Test
    void listarTodosRetornaTodosLosProductos() {
        FakeInventarioGateway gateway = new FakeInventarioGateway();
        gateway.guardar(crearInventario("PROD-001", "Laptop", 50, 10));
        gateway.guardar(crearInventario("PROD-002", "Mouse", 200, 20));
        InventarioUseCase useCase = new InventarioUseCase(gateway);

        List<Inventario> resultado = useCase.listarTodos();

        assertEquals(2, resultado.size());
    }

    @Test
    void listarTodosRetornaListaVaciaCuandoNoHayProductos() {
        InventarioUseCase useCase = new InventarioUseCase(new FakeInventarioGateway());

        List<Inventario> resultado = useCase.listarTodos();

        assertTrue(resultado.isEmpty());
    }

    @Test
    void listarConAlertaStockBajoRetornaSoloProductosConAlerta() {
        FakeInventarioGateway gateway = new FakeInventarioGateway();
        Inventario inventario1 = crearInventario("PROD-001", "Laptop", 5, 10);
        inventario1.setAlertaStockBajo(true);
        Inventario inventario2 = crearInventario("PROD-002", "Mouse", 200, 20);
        inventario2.setAlertaStockBajo(false);
        Inventario inventario3 = crearInventario("PROD-003", "Teclado", 8, 15);
        inventario3.setAlertaStockBajo(true);
        gateway.guardar(inventario1);
        gateway.guardar(inventario2);
        gateway.guardar(inventario3);
        InventarioUseCase useCase = new InventarioUseCase(gateway);

        List<Inventario> resultado = useCase.listarConAlertaStockBajo();

        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(Inventario::getAlertaStockBajo));
    }

    @Test
    void listarConAlertaStockBajoRetornaListaVaciaCuandoNoHayAlertas() {
        FakeInventarioGateway gateway = new FakeInventarioGateway();
        Inventario inventario = crearInventario("PROD-001", "Laptop", 50, 10);
        inventario.setAlertaStockBajo(false);
        gateway.guardar(inventario);
        InventarioUseCase useCase = new InventarioUseCase(gateway);

        List<Inventario> resultado = useCase.listarConAlertaStockBajo();

        assertTrue(resultado.isEmpty());
    }

    @Test
    void aumentarStockIncrementaCorrectamente() {
        FakeInventarioGateway gateway = new FakeInventarioGateway();
        gateway.guardar(crearInventario("PROD-001", "Laptop", 50, 10));
        InventarioUseCase useCase = new InventarioUseCase(gateway);

        Inventario resultado = useCase.aumentarStock("PROD-001", 25);

        assertEquals(75, resultado.getStockActual());
        assertFalse(resultado.getAlertaStockBajo());
    }

    @Test
    void aumentarStockActivaAlertaCuandoStockQuedaEnMinimo() {
        FakeInventarioGateway gateway = new FakeInventarioGateway();
        Inventario inventario = crearInventario("PROD-001", "Laptop", 5, 10);
        inventario.setAlertaStockBajo(true);
        gateway.guardar(inventario);
        InventarioUseCase useCase = new InventarioUseCase(gateway);

        Inventario resultado = useCase.aumentarStock("PROD-001", 5);

        assertEquals(10, resultado.getStockActual());
        assertTrue(resultado.getAlertaStockBajo());
    }

    @Test
    void aumentarStockDesactivaAlertaCuandoStockSuperaMinimo() {
        FakeInventarioGateway gateway = new FakeInventarioGateway();
        Inventario inventario = crearInventario("PROD-001", "Laptop", 5, 10);
        inventario.setAlertaStockBajo(true);
        gateway.guardar(inventario);
        InventarioUseCase useCase = new InventarioUseCase(gateway);

        Inventario resultado = useCase.aumentarStock("PROD-001", 10);

        assertEquals(15, resultado.getStockActual());
        assertFalse(resultado.getAlertaStockBajo());
    }

    @Test
    void aumentarStockFallaCuandoCantidadEsNula() {
        FakeInventarioGateway gateway = new FakeInventarioGateway();
        gateway.guardar(crearInventario("PROD-001", "Laptop", 50, 10));
        InventarioUseCase useCase = new InventarioUseCase(gateway);

        DatosInventarioInvalidosException error = assertThrows(
                DatosInventarioInvalidosException.class,
                () -> useCase.aumentarStock("PROD-001", null)
        );

        assertEquals("La cantidad a aumentar debe ser mayor a 0", error.getMessage());
    }

    @Test
    void aumentarStockFallaCuandoCantidadEsCero() {
        FakeInventarioGateway gateway = new FakeInventarioGateway();
        gateway.guardar(crearInventario("PROD-001", "Laptop", 50, 10));
        InventarioUseCase useCase = new InventarioUseCase(gateway);

        DatosInventarioInvalidosException error = assertThrows(
                DatosInventarioInvalidosException.class,
                () -> useCase.aumentarStock("PROD-001", 0)
        );

        assertEquals("La cantidad a aumentar debe ser mayor a 0", error.getMessage());
    }

    @Test
    void aumentarStockFallaCuandoCantidadEsNegativa() {
        FakeInventarioGateway gateway = new FakeInventarioGateway();
        gateway.guardar(crearInventario("PROD-001", "Laptop", 50, 10));
        InventarioUseCase useCase = new InventarioUseCase(gateway);

        DatosInventarioInvalidosException error = assertThrows(
                DatosInventarioInvalidosException.class,
                () -> useCase.aumentarStock("PROD-001", -5)
        );

        assertEquals("La cantidad a aumentar debe ser mayor a 0", error.getMessage());
    }

    @Test
    void aumentarStockFallaCuandoProductoNoExiste() {
        InventarioUseCase useCase = new InventarioUseCase(new FakeInventarioGateway());

        ProductoInventarioNoEncontradoException error = assertThrows(
                ProductoInventarioNoEncontradoException.class,
                () -> useCase.aumentarStock("PROD-999", 10)
        );

        assertEquals("Producto no encontrado en inventario: PROD-999", error.getMessage());
    }

    @Test
    void reducirStockDecrementaCorrectamente() {
        FakeInventarioGateway gateway = new FakeInventarioGateway();
        gateway.guardar(crearInventario("PROD-001", "Laptop", 50, 10));
        InventarioUseCase useCase = new InventarioUseCase(gateway);

        Inventario resultado = useCase.reducirStock("PROD-001", 20);

        assertEquals(30, resultado.getStockActual());
        assertFalse(resultado.getAlertaStockBajo());
    }

    @Test
    void reducirStockActivaAlertaCuandoStockquedaEnMinimo() {
        FakeInventarioGateway gateway = new FakeInventarioGateway();
        Inventario inventario = crearInventario("PROD-001", "Laptop", 20, 10);
        inventario.setAlertaStockBajo(false);
        gateway.guardar(inventario);
        InventarioUseCase useCase = new InventarioUseCase(gateway);

        Inventario resultado = useCase.reducirStock("PROD-001", 10);

        assertEquals(10, resultado.getStockActual());
        assertTrue(resultado.getAlertaStockBajo());
    }

    @Test
    void reducirStockActivaAlertaCuandoStockquedaPorDebajoDelMinimo() {
        FakeInventarioGateway gateway = new FakeInventarioGateway();
        Inventario inventario = crearInventario("PROD-001", "Laptop", 20, 10);
        inventario.setAlertaStockBajo(false);
        gateway.guardar(inventario);
        InventarioUseCase useCase = new InventarioUseCase(gateway);

        Inventario resultado = useCase.reducirStock("PROD-001", 15);

        assertEquals(5, resultado.getStockActual());
        assertTrue(resultado.getAlertaStockBajo());
    }

    @Test
    void reducirStockFallaCuandoCantidadEsNula() {
        FakeInventarioGateway gateway = new FakeInventarioGateway();
        gateway.guardar(crearInventario("PROD-001", "Laptop", 50, 10));
        InventarioUseCase useCase = new InventarioUseCase(gateway);

        DatosInventarioInvalidosException error = assertThrows(
                DatosInventarioInvalidosException.class,
                () -> useCase.reducirStock("PROD-001", null)
        );

        assertEquals("La cantidad a reducir debe ser mayor a 0", error.getMessage());
    }

    @Test
    void reducirStockFallaCuandoCantidadEsCero() {
        FakeInventarioGateway gateway = new FakeInventarioGateway();
        gateway.guardar(crearInventario("PROD-001", "Laptop", 50, 10));
        InventarioUseCase useCase = new InventarioUseCase(gateway);

        DatosInventarioInvalidosException error = assertThrows(
                DatosInventarioInvalidosException.class,
                () -> useCase.reducirStock("PROD-001", 0)
        );

        assertEquals("La cantidad a reducir debe ser mayor a 0", error.getMessage());
    }

    @Test
    void reducirStockFallaCuandoCantidadEsNegativa() {
        FakeInventarioGateway gateway = new FakeInventarioGateway();
        gateway.guardar(crearInventario("PROD-001", "Laptop", 50, 10));
        InventarioUseCase useCase = new InventarioUseCase(gateway);

        DatosInventarioInvalidosException error = assertThrows(
                DatosInventarioInvalidosException.class,
                () -> useCase.reducirStock("PROD-001", -3)
        );

        assertEquals("La cantidad a reducir debe ser mayor a 0", error.getMessage());
    }

    @Test
    void reducirStockFallaCuandoStockEsInsuficiente() {
        FakeInventarioGateway gateway = new FakeInventarioGateway();
        gateway.guardar(crearInventario("PROD-001", "Laptop", 5, 10));
        InventarioUseCase useCase = new InventarioUseCase(gateway);

        StockInsuficienteException error = assertThrows(
                StockInsuficienteException.class,
                () -> useCase.reducirStock("PROD-001", 10)
        );

        assertEquals("Stock insuficiente para el producto PROD-001. Stock actual: 5, cantidad solicitada: 10", error.getMessage());
    }

    @Test
    void reducirStockFallaCuandoProductoNoExiste() {
        InventarioUseCase useCase = new InventarioUseCase(new FakeInventarioGateway());

        ProductoInventarioNoEncontradoException error = assertThrows(
                ProductoInventarioNoEncontradoException.class,
                () -> useCase.reducirStock("PROD-999", 10)
        );

        assertEquals("Producto no encontrado en inventario: PROD-999", error.getMessage());
    }

    @Test
    void eliminarProductoEliminaCuandoExiste() {
        FakeInventarioGateway gateway = new FakeInventarioGateway();
        gateway.guardar(crearInventario("PROD-001", "Laptop", 50, 10));
        InventarioUseCase useCase = new InventarioUseCase(gateway);

        useCase.eliminarProducto("PROD-001");

        assertEquals("PROD-001", gateway.productoEliminado);
    }

    @Test
    void eliminarProductoFallaCuandoNoExiste() {
        InventarioUseCase useCase = new InventarioUseCase(new FakeInventarioGateway());

        ProductoInventarioNoEncontradoException error = assertThrows(
                ProductoInventarioNoEncontradoException.class,
                () -> useCase.eliminarProducto("PROD-999")
        );

        assertEquals("Producto no encontrado en inventario: PROD-999", error.getMessage());
    }

    private Inventario crearInventario(String productoId, String nombreProducto, Integer stockActual, Integer stockMinimo) {
        return new Inventario(
                null,
                productoId,
                nombreProducto,
                stockActual,
                stockMinimo,
                null
        );
    }

    private static class FakeInventarioGateway implements InventarioGateway {
        private final Map<String, Inventario> inventarios = new ConcurrentHashMap<>();
        private Inventario inventarioGuardado;
        private String productoEliminado;

        @Override
        public Inventario guardar(Inventario inventario) {
            inventarioGuardado = inventario;
            inventarios.put(inventario.getProductoId(), inventario);
            return inventario;
        }

        @Override
        public Inventario buscarPorProductoId(String productoId) {
            return inventarios.get(productoId);
        }

        @Override
        public List<Inventario> listarTodos() {
            return new ArrayList<>(inventarios.values());
        }

        @Override
        public List<Inventario> listarConAlertaStockBajo() {
            return inventarios.values().stream()
                    .filter(Inventario::getAlertaStockBajo)
                    .collect(Collectors.toList());
        }

        @Override
        public void eliminar(String productoId) {
            productoEliminado = productoId;
            inventarios.remove(productoId);
        }
    }
}
