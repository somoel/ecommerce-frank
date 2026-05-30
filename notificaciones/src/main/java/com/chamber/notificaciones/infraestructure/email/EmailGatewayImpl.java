package com.chamber.notificaciones.infraestructure.email;

import com.chamber.notificaciones.domain.model.Notificacion;
import com.chamber.notificaciones.domain.model.gateway.NotificacionGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailGatewayImpl implements NotificacionGateway {

    private final JavaMailSender mailSender;

    @Override
    public void enviarEmail(Notificacion notificacion) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(notificacion.getDestinatarioEmail());
        message.setSubject(notificacion.getAsunto());
        message.setText(notificacion.getMensaje());
        mailSender.send(message);
    }
}