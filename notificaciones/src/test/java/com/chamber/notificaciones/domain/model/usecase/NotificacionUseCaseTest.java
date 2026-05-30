package com.chamber.notificaciones.domain.model.usecase;

import com.chamber.notificaciones.domain.model.Notificacion;
import com.chamber.notificaciones.domain.model.exception.NotificacionException;
import com.chamber.notificaciones.domain.model.gateway.NotificacionGateway;
import com.chamber.notificaciones.domain.model.gateway.NotificacionRepositoryGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class NotificacionUseCaseTest {

    @Mock
    private NotificacionGateway notificacionGateway;

    @Mock
    private NotificacionRepositoryGateway repositoryGateway;

    @InjectMocks
    private NotificacionUseCase notificacionUseCase;

    @Test
    void enviarNotificacionGuardaNotificacionYRetornaEnviada() {
        FakeNotificacionGateway notificacionGateway = new FakeNotificacionGateway();
        FakeNotificacionRepositoryGateway repositoryGateway = new FakeNotificacionRepositoryGateway();
        NotificacionUseCase notificacionUseCase = new NotificacionUseCase(notificacionGateway, repositoryGateway);
        Notificacion notificacion = crearNotificacion("test@example.com", "ORDEN_CREADA", "Tu orden fue creada");

        Notificacion resultado = notificacionUseCase.enviarNotificacion(notificacion);

        assertTrue(resultado.getEnviada());
        assertNotNull(resultado.getFechaEnvio());
        assertEquals("test@example.com", repositoryGateway.notificacionGuardada.getDestinatarioEmail());
    }

    @Test
    void enviarNotificacionMarcaEnviadaFalseCuandoEmailFalla() {
        FakeNotificacionGateway notificacionGateway = new FakeNotificacionGateway();
        notificacionGateway.debeFallar = true;
        FakeNotificacionRepositoryGateway repositoryGateway = new FakeNotificacionRepositoryGateway();
        NotificacionUseCase notificacionUseCase = new NotificacionUseCase(notificacionGateway, repositoryGateway);
        Notificacion notificacion = crearNotificacion("test@example.com", "ORDEN_CREADA", "Tu orden fue creada");

        Notificacion resultado = notificacionUseCase.enviarNotificacion(notificacion);

        assertFalse(resultado.getEnviada());
        assertNotNull(resultado.getFechaEnvio());
        assertNotNull(repositoryGateway.notificacionGuardada);
    }

    @Test
    void enviarNotificacionFallaCuandoNotificacionEsNula() {
        NotificacionUseCase notificacionUseCase = new NotificacionUseCase(
                new FakeNotificacionGateway(), new FakeNotificacionRepositoryGateway());

        NotificacionException error = assertThrows(
                NotificacionException.class,
                () -> notificacionUseCase.enviarNotificacion(null)
        );

        assertEquals("La notificación no puede ser nula", error.getMessage());
    }

    @Test
    void enviarNotificacionFallaCuandoDestinatarioEmailEsInvalido() {
        NotificacionUseCase notificacionUseCase = new NotificacionUseCase(
                new FakeNotificacionGateway(), new FakeNotificacionRepositoryGateway());

        assertThrows(NotificacionException.class, () -> notificacionUseCase.enviarNotificacion(
                crearNotificacion(null, "ORDEN_CREADA", "Tu orden fue creada")
        ));
        assertThrows(NotificacionException.class, () -> notificacionUseCase.enviarNotificacion(
                crearNotificacion(" ", "ORDEN_CREADA", "Tu orden fue creada")
        ));
    }

    @Test
    void enviarNotificacionFallaCuandoTipoEsInvalido() {
        NotificacionUseCase notificacionUseCase = new NotificacionUseCase(
                new FakeNotificacionGateway(), new FakeNotificacionRepositoryGateway());

        assertThrows(NotificacionException.class, () -> notificacionUseCase.enviarNotificacion(
                crearNotificacion("test@example.com", null, "Tu orden fue creada")
        ));
        assertThrows(NotificacionException.class, () -> notificacionUseCase.enviarNotificacion(
                crearNotificacion("test@example.com", " ", "Tu orden fue creada")
        ));
    }

    @Test
    void enviarNotificacionFallaCuandoMensajeEsInvalido() {
        NotificacionUseCase notificacionUseCase = new NotificacionUseCase(
                new FakeNotificacionGateway(), new FakeNotificacionRepositoryGateway());

        assertThrows(NotificacionException.class, () -> notificacionUseCase.enviarNotificacion(
                crearNotificacion("test@example.com", "ORDEN_CREADA", null)
        ));
        assertThrows(NotificacionException.class, () -> notificacionUseCase.enviarNotificacion(
                crearNotificacion("test@example.com", "ORDEN_CREADA", " ")
        ));
    }

    @Test
    void buscarPorEmailRetornaListaDeNotificaciones() {
        FakeNotificacionRepositoryGateway repositoryGateway = new FakeNotificacionRepositoryGateway();
        repositoryGateway.notificacionesPorEmail = List.of(
                crearNotificacion("test@example.com", "ORDEN_CREADA", "Tu orden fue creada"),
                crearNotificacion("test@example.com", "ORDEN_ENVIADA", "Tu orden fue enviada")
        );
        NotificacionUseCase notificacionUseCase = new NotificacionUseCase(
                new FakeNotificacionGateway(), repositoryGateway);

        List<Notificacion> resultado = notificacionUseCase.buscarPorEmail("test@example.com");

        assertEquals(2, resultado.size());
        assertEquals("test@example.com", resultado.get(0).getDestinatarioEmail());
    }

    @Test
    void buscarPorEmailFallaCuandoEmailEsInvalido() {
        NotificacionUseCase notificacionUseCase = new NotificacionUseCase(
                new FakeNotificacionGateway(), new FakeNotificacionRepositoryGateway());

        NotificacionException error = assertThrows(
                NotificacionException.class,
                () -> notificacionUseCase.buscarPorEmail(null)
        );

        assertEquals("El email es obligatorio", error.getMessage());

        assertThrows(NotificacionException.class, () -> notificacionUseCase.buscarPorEmail(" "));
    }

    private Notificacion crearNotificacion(String destinatarioEmail, String tipo, String mensaje) {
        return new Notificacion(
                null,
                destinatarioEmail,
                tipo,
                "Asunto de prueba",
                mensaje,
                null,
                null
        );
    }

    private static class FakeNotificacionGateway implements NotificacionGateway {
        private boolean debeFallar = false;

        @Override
        public void enviarEmail(Notificacion notificacion) {
            if (debeFallar) {
                throw new RuntimeException("Error al enviar email");
            }
        }
    }

    private static class FakeNotificacionRepositoryGateway implements NotificacionRepositoryGateway {
        private Notificacion notificacionGuardada;
        private List<Notificacion> notificacionesPorEmail = List.of();

        @Override
        public Notificacion guardar(Notificacion notificacion) {
            notificacionGuardada = notificacion;
            return notificacion;
        }

        @Override
        public List<Notificacion> buscarPorEmail(String email) {
            return notificacionesPorEmail;
        }
    }
}
