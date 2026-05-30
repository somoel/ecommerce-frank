package com.chamber.inventario.infraestructure.entry_points;

import com.chamber.inventario.domain.model.Inventario;
import com.chamber.inventario.domain.model.usecase.InventarioUseCase;
import com.chamber.inventario.infraestructure.entry_points.dto.ActualizarStockRequest;
import com.chamber.inventario.infraestructure.entry_points.dto.InventarioRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/ecommerce/inventario")
@RequiredArgsConstructor
public class InventarioController {
    private final InventarioUseCase inventarioUseCase;

    @PostMapping("/registrar")
    public ResponseEntity<Inventario> registrar(@RequestBody InventarioRequest request) {
        Inventario inventario = new Inventario();
        inventario.setProductoId(request.getProductoId());
        inventario.setNombreProducto(request.getNombreProducto());
        inventario.setStockActual(request.getStockActual());
        inventario.setStockMinimo(request.getStockMinimo());
        return new ResponseEntity<>(inventarioUseCase.registrarProducto(inventario), HttpStatus.CREATED);
    }

    @GetMapping("/{productoId}")
    public ResponseEntity<Inventario> buscar(@PathVariable String productoId) {
        return new ResponseEntity<>(inventarioUseCase.buscarPorProductoId(productoId), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Inventario>> listarTodos() {
        return new ResponseEntity<>(inventarioUseCase.listarTodos(), HttpStatus.OK);
    }

    @GetMapping("/alertas")
    public ResponseEntity<List<Inventario>> listarAlertas() {
        return new ResponseEntity<>(inventarioUseCase.listarConAlertaStockBajo(), HttpStatus.OK);
    }

    @PatchMapping("/{productoId}/aumentar")
    public ResponseEntity<Inventario> aumentarStock(
            @PathVariable String productoId,
            @RequestBody ActualizarStockRequest request) {
        return new ResponseEntity<>(inventarioUseCase.aumentarStock(productoId, request.getCantidad()), HttpStatus.OK);
    }

    @PatchMapping("/{productoId}/reducir")
    public ResponseEntity<Inventario> reducirStock(
            @PathVariable String productoId,
            @RequestBody ActualizarStockRequest request) {
        return new ResponseEntity<>(inventarioUseCase.reducirStock(productoId, request.getCantidad()), HttpStatus.OK);
    }

    @DeleteMapping("/{productoId}")
    public ResponseEntity<String> eliminar(@PathVariable String productoId) {
        inventarioUseCase.eliminarProducto(productoId);
        return new ResponseEntity<>("Producto eliminado del inventario", HttpStatus.OK);
    }
}
