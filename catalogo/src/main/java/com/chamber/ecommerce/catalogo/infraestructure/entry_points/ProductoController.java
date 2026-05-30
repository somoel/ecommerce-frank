package com.chamber.ecommerce.catalogo.infraestructure.entry_points;

import com.chamber.ecommerce.catalogo.domain.model.Producto;
import com.chamber.ecommerce.catalogo.domain.model.usecase.ProductoUseCase;
import com.chamber.ecommerce.catalogo.infraestructure.entry_points.dto.ProductoRequest;
import com.chamber.ecommerce.catalogo.infraestructure.mapper.ProductoMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/ecommerce/producto")
@RequiredArgsConstructor
public class ProductoController {
    private final ProductoUseCase productoUseCase;
    private final ProductoMapper productoMapper;

    @GetMapping("/all")
    public ResponseEntity<List<Producto>> listarProductos() {
        return new ResponseEntity<>(productoUseCase.listarProductos(), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveProducto(@RequestBody ProductoRequest productoRequest) {
        try {
            Producto producto = productoMapper.toProducto(productoRequest);
            Producto productoGuardado = productoUseCase.guardarProducto(producto);
            return new ResponseEntity<>(productoGuardado, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> buscarById(@PathVariable String id) {
        Producto producto = productoUseCase.buscarProducto(id);
        if (producto != null) {
            return new ResponseEntity<>(producto, HttpStatus.OK);
        }
        return new ResponseEntity<>(producto, HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProducto(@PathVariable String id, @RequestBody ProductoRequest productoRequest) {
        try {
            Producto producto = productoMapper.toProducto(productoRequest);
            Producto actualizado = productoUseCase.actualizarProducto(id, producto);
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable String id) {
        try {
            productoUseCase.eliminarProducto(id);
            return new ResponseEntity<>("Producto eliminado", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Producto no encontrado", HttpStatus.NOT_FOUND);
        }
    }
}
