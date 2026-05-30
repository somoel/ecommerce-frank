package com.chamber.ecommerce.catalogo.domain.model.usecase;

import com.chamber.ecommerce.catalogo.domain.model.Producto;
import com.chamber.ecommerce.catalogo.domain.model.gateway.ProductoGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ProductoUseCaseTest {

    @Mock
    private ProductoGateway productoGateway;

    @InjectMocks
    private ProductoUseCase productoUseCase;

    @Test
    void guardarProductoPersisteCuandoDatosValidos() {
        FakeProductoGateway productoGateway = new FakeProductoGateway();
        ProductoUseCase productoUseCase = new ProductoUseCase(productoGateway);
        Producto producto = crearProducto("1", "Laptop", "Laptop gamer", 3500.0, 10);

        Producto resultado = productoUseCase.guardarProducto(producto);

        assertNotNull(resultado);
        assertEquals("Laptop", resultado.getNombre());
        assertEquals(3500.0, resultado.getPrecio());
        assertEquals(10, resultado.getStock());
        assertEquals("Laptop", productoGateway.productoGuardado.getNombre());
    }

    @Test
    void guardarProductoFallaCuandoNombreEsNulo() {
        ProductoUseCase productoUseCase = new ProductoUseCase(new FakeProductoGateway());
        Producto producto = crearProducto("1", null, "Descripcion", 100.0, 5);

        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> productoUseCase.guardarProducto(producto)
        );

        assertEquals("El nombre no puede ser nulo o vacio", error.getMessage());
    }

    @Test
    void guardarProductoFallaCuandoNombreEsVacio() {
        ProductoUseCase productoUseCase = new ProductoUseCase(new FakeProductoGateway());
        Producto producto = crearProducto("1", " ", "Descripcion", 100.0, 5);

        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> productoUseCase.guardarProducto(producto)
        );

        assertEquals("El nombre no puede ser nulo o vacio", error.getMessage());
    }

    @Test
    void guardarProductoFallaCuandoPrecioEsNulo() {
        ProductoUseCase productoUseCase = new ProductoUseCase(new FakeProductoGateway());
        Producto producto = crearProducto("1", "Laptop", "Descripcion", null, 5);

        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> productoUseCase.guardarProducto(producto)
        );

        assertEquals("El precio debe ser mayor o igual a 0", error.getMessage());
    }

    @Test
    void guardarProductoFallaCuandoPrecioEsNegativo() {
        ProductoUseCase productoUseCase = new ProductoUseCase(new FakeProductoGateway());
        Producto producto = crearProducto("1", "Laptop", "Descripcion", -100.0, 5);

        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> productoUseCase.guardarProducto(producto)
        );

        assertEquals("El precio debe ser mayor o igual a 0", error.getMessage());
    }

    @Test
    void guardarProductoFallaCuandoStockEsNulo() {
        ProductoUseCase productoUseCase = new ProductoUseCase(new FakeProductoGateway());
        Producto producto = crearProducto("1", "Laptop", "Descripcion", 100.0, null);

        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> productoUseCase.guardarProducto(producto)
        );

        assertEquals("El stock debe ser mayor o igual a 0", error.getMessage());
    }

    @Test
    void guardarProductoFallaCuandoStockEsNegativo() {
        ProductoUseCase productoUseCase = new ProductoUseCase(new FakeProductoGateway());
        Producto producto = crearProducto("1", "Laptop", "Descripcion", 100.0, -1);

        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> productoUseCase.guardarProducto(producto)
        );

        assertEquals("El stock debe ser mayor o igual a 0", error.getMessage());
    }

    @Test
    void guardarProductoAceptaPrecioYStockEnCero() {
        FakeProductoGateway productoGateway = new FakeProductoGateway();
        ProductoUseCase productoUseCase = new ProductoUseCase(productoGateway);
        Producto producto = crearProducto("1", "Laptop", "Descripcion", 0.0, 0);

        Producto resultado = productoUseCase.guardarProducto(producto);

        assertNotNull(resultado);
        assertEquals(0.0, resultado.getPrecio());
        assertEquals(0, resultado.getStock());
    }

    @Test
    void buscarProductoRetornaProductoExistente() {
        FakeProductoGateway productoGateway = new FakeProductoGateway();
        productoGateway.productoPorId = crearProducto("1", "Laptop", "Laptop gamer", 3500.0, 10);
        ProductoUseCase productoUseCase = new ProductoUseCase(productoGateway);

        Producto resultado = productoUseCase.buscarProducto("1");

        assertEquals("1", resultado.getId());
        assertEquals("Laptop", resultado.getNombre());
    }

    @Test
    void buscarProductoRetornaProductoVacioCuandoNoExiste() {
        ProductoUseCase productoUseCase = new ProductoUseCase(new FakeProductoGateway());

        Producto resultado = productoUseCase.buscarProducto("999");

        assertNotNull(resultado);
        assertEquals(null, resultado.getId());
        assertEquals(null, resultado.getNombre());
    }

    @Test
    void eliminarProductoEliminaCuandoExiste() {
        FakeProductoGateway productoGateway = new FakeProductoGateway();
        ProductoUseCase productoUseCase = new ProductoUseCase(productoGateway);

        productoUseCase.eliminarProducto("1");

        assertEquals("1", productoGateway.idEliminado);
    }

    @Test
    void eliminarProductoNoLanzaExcepcionCuandoNoExiste() {
        ProductoUseCase productoUseCase = new ProductoUseCase(new FakeProductoGateway());

        productoUseCase.eliminarProducto("999");
    }

    private Producto crearProducto(String id, String nombre, String descripcion, Double precio, Integer stock) {
        Producto producto = new Producto();
        producto.setId(id);
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setPrecio(precio);
        producto.setStock(stock);
        return producto;
    }

    private static class FakeProductoGateway implements ProductoGateway {
        private Producto productoGuardado;
        private Producto productoPorId;
        private String idEliminado;

        @Override
        public Producto guardarProducto(Producto producto) {
            productoGuardado = producto;
            return producto;
        }

        @Override
        public Producto buscarProducto(String id) {
            if (productoPorId == null || !productoPorId.getId().equals(id)) {
                throw new RuntimeException("Producto no encontrado");
            }
            return productoPorId;
        }

        @Override
        public void eliminarProducto(String id) {
            idEliminado = id;
        }
    }
}
