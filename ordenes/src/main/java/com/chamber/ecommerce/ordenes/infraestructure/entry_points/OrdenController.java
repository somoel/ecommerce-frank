package com.chamber.ecommerce.ordenes.infraestructure.entry_points;

import com.chamber.ecommerce.ordenes.domain.model.ItemOrden;
import com.chamber.ecommerce.ordenes.domain.model.Orden;
import com.chamber.ecommerce.ordenes.domain.model.usecase.OrdenUseCase;
import com.chamber.ecommerce.ordenes.infraestructure.entry_points.EstadoRequest;
import com.chamber.ecommerce.ordenes.infraestructure.entry_points.OrdenRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/ecommerce/orden")
@RequiredArgsConstructor
public class OrdenController {
    private final OrdenUseCase ordenUseCase;

    @PostMapping("/crear")
    public ResponseEntity<Orden> crearOrden(@RequestBody OrdenRequest request) {
        Orden orden = new Orden();
        orden.setCedulaUsuario(request.getCedulaUsuario());
        List<ItemOrden> items = request.getItems().stream()
                .map(i -> new ItemOrden(i.getProductoId(), i.getCantidad(), i.getPrecioUnitario()))
                .collect(Collectors.toList());
        orden.setItems(items);
        Orden creada = ordenUseCase.crearOrden(orden);
        return new ResponseEntity<>(creada, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Orden> buscarById(@PathVariable String id) {
        return new ResponseEntity<>(ordenUseCase.buscarOrden(id), HttpStatus.OK);
    }

    @GetMapping("/usuario/{cedula}")
    public ResponseEntity<List<Orden>> buscarPorUsuario(@PathVariable String cedula) {
        return new ResponseEntity<>(ordenUseCase.buscarPorUsuario(cedula), HttpStatus.OK);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Orden> actualizarEstado(@PathVariable String id, @RequestBody EstadoRequest request) {
        return new ResponseEntity<>(ordenUseCase.actualizarEstado(id, request.getEstado()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarOrden(@PathVariable String id) {
        ordenUseCase.eliminarOrden(id);
        return new ResponseEntity<>("Orden eliminada", HttpStatus.OK);
    }
}

