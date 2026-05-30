package com.chamber.notificaciones.infraestructure.entry_points;

import com.chamber.notificaciones.domain.model.Notificacion;
import com.chamber.notificaciones.domain.model.usecase.NotificacionUseCase;
import com.chamber.notificaciones.infraestructure.entry_points.dto.NotificacionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/ecommerce/notificacion")
@RequiredArgsConstructor
public class NotificacionController {
    private final NotificacionUseCase notificacionUseCase;

    @PostMapping("/enviar")
    public ResponseEntity<Notificacion> enviar(@RequestBody NotificacionRequest request) {
        Notificacion notificacion = new Notificacion();
        notificacion.setDestinatarioEmail(request.getDestinatarioEmail());
        notificacion.setTipo(request.getTipo());
        notificacion.setAsunto(request.getAsunto());
        notificacion.setMensaje(request.getMensaje());
        return new ResponseEntity<>(notificacionUseCase.enviarNotificacion(notificacion), HttpStatus.OK);
    }

    @GetMapping("/usuario/{email}")
    public ResponseEntity<List<Notificacion>> buscarPorEmail(@PathVariable String email) {
        return new ResponseEntity<>(notificacionUseCase.buscarPorEmail(email), HttpStatus.OK);
    }
}
