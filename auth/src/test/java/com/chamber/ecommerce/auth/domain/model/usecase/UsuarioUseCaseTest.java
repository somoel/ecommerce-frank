package com.chamber.ecommerce.auth.domain.model.usecase;

import com.chamber.ecommerce.auth.domain.model.Usuario;
import com.chamber.ecommerce.auth.domain.model.exception.DatosInvalidosException;
import com.chamber.ecommerce.auth.domain.model.exception.UsuarioNoEncontradoException;
import com.chamber.ecommerce.auth.domain.model.exception.UsuarioYaExisteException;
import com.chamber.ecommerce.auth.domain.model.gateway.SecurityGateway;
import com.chamber.ecommerce.auth.domain.model.gateway.UsuarioGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class UsuarioUseCaseTest {

    @Mock
    private UsuarioGateway usuarioGateway;

    @Mock
    private SecurityGateway securityGateway;

    @InjectMocks
    private UsuarioUseCase usuarioUseCase;

    @Test
    void guardarUsuarioEncriptaPasswordAntesDePersistir() {
        FakeUsuarioGateway usuarioGateway = new FakeUsuarioGateway();
        UsuarioUseCase usuarioUseCase = new UsuarioUseCase(usuarioGateway, new FakeSecurityGateway());
        Usuario usuario = crearUsuario("1234567890", "juan.perez@example.com", "Clave123");

        Usuario resultado = usuarioUseCase.guardarUsuario(usuario);

        assertEquals("encrypted:Clave123", resultado.getPassword());
        assertEquals("encrypted:Clave123", usuarioGateway.usuarioGuardado.getPassword());
        assertNotEquals("Clave123", resultado.getPassword());
    }

    @Test
    void guardarUsuarioFallaCuandoUsuarioEsNulo() {
        UsuarioUseCase usuarioUseCase = new UsuarioUseCase(new FakeUsuarioGateway(), new FakeSecurityGateway());

        DatosInvalidosException error = assertThrows(
                DatosInvalidosException.class,
                () -> usuarioUseCase.guardarUsuario(null)
        );

        assertEquals("Usuario no puede ser nulo", error.getMessage());
    }

    @Test
    void guardarUsuarioFallaConEmailOPasswordInvalidos() {
        UsuarioUseCase usuarioUseCase = new UsuarioUseCase(new FakeUsuarioGateway(), new FakeSecurityGateway());

        assertThrows(DatosInvalidosException.class, () -> usuarioUseCase.guardarUsuario(
                crearUsuario("1234567890", null, "Clave123")
        ));
        assertThrows(DatosInvalidosException.class, () -> usuarioUseCase.guardarUsuario(
                crearUsuario("1234567890", " ", "Clave123")
        ));
        assertThrows(DatosInvalidosException.class, () -> usuarioUseCase.guardarUsuario(
                crearUsuario("1234567890", "juan.perez@example.com", null)
        ));
        assertThrows(DatosInvalidosException.class, () -> usuarioUseCase.guardarUsuario(
                crearUsuario("1234567890", "juan.perez@example.com", " ")
        ));
    }

    @Test
    void guardarUsuarioFallaCuandoCedulaYaExiste() {
        FakeUsuarioGateway usuarioGateway = new FakeUsuarioGateway();
        usuarioGateway.usuarioPorCedula = crearUsuario("1234567890", "existente@example.com", "encrypted:Clave123");
        UsuarioUseCase usuarioUseCase = new UsuarioUseCase(usuarioGateway, new FakeSecurityGateway());

        UsuarioYaExisteException error = assertThrows(
                UsuarioYaExisteException.class,
                () -> usuarioUseCase.guardarUsuario(crearUsuario("1234567890", "nuevo@example.com", "Clave123"))
        );

        assertEquals("El usuario ya existe", error.getMessage());
    }

    @Test
    void guardarUsuarioFallaCuandoEmailYaExiste() {
        FakeUsuarioGateway usuarioGateway = new FakeUsuarioGateway();
        usuarioGateway.usuarioPorEmail = crearUsuario("9999999999", "juan.perez@example.com", "encrypted:Clave123");
        UsuarioUseCase usuarioUseCase = new UsuarioUseCase(usuarioGateway, new FakeSecurityGateway());

        UsuarioYaExisteException error = assertThrows(
                UsuarioYaExisteException.class,
                () -> usuarioUseCase.guardarUsuario(crearUsuario("1234567890", "juan.perez@example.com", "Clave123"))
        );

        assertEquals("El email ya esta registrado", error.getMessage());
    }

    @Test
    void buscarUsuarioRetornaUsuarioExistente() {
        FakeUsuarioGateway usuarioGateway = new FakeUsuarioGateway();
        usuarioGateway.usuarioPorCedula = crearUsuario("1234567890", "juan.perez@example.com", "encrypted:Clave123");
        UsuarioUseCase usuarioUseCase = new UsuarioUseCase(usuarioGateway, new FakeSecurityGateway());

        Usuario resultado = usuarioUseCase.buscarUsuario("1234567890");

        assertEquals("1234567890", resultado.getCedula());
        assertEquals("juan.perez@example.com", resultado.getEmail());
    }

    @Test
    void buscarUsuarioFallaCuandoNoExiste() {
        UsuarioUseCase usuarioUseCase = new UsuarioUseCase(new FakeUsuarioGateway(), new FakeSecurityGateway());

        UsuarioNoEncontradoException error = assertThrows(
                UsuarioNoEncontradoException.class,
                () -> usuarioUseCase.buscarUsuario("1234567890")
        );

        assertEquals("Usuario no encontrado", error.getMessage());
    }

    @Test
    void loginRetornaTrueConCredencialesValidas() {
        FakeUsuarioGateway usuarioGateway = new FakeUsuarioGateway();
        usuarioGateway.usuarioPorEmail = crearUsuario("1234567890", "juan.perez@example.com", "encrypted:Clave123");
        UsuarioUseCase usuarioUseCase = new UsuarioUseCase(usuarioGateway, new FakeSecurityGateway());

        boolean resultado = usuarioUseCase.login("juan.perez@example.com", "Clave123");

        assertTrue(resultado);
    }

    @Test
    void loginRetornaFalseCuandoUsuarioNoExisteOPasswordNoCoincide() {
        FakeUsuarioGateway usuarioGateway = new FakeUsuarioGateway();
        UsuarioUseCase usuarioUseCase = new UsuarioUseCase(usuarioGateway, new FakeSecurityGateway());

        assertFalse(usuarioUseCase.login("juan.perez@example.com", "Clave123"));

        usuarioGateway.usuarioPorEmail = crearUsuario("1234567890", "juan.perez@example.com", "encrypted:Clave123");

        assertFalse(usuarioUseCase.login("juan.perez@example.com", "OtraClave"));
    }

    @Test
    void loginRetornaFalseConDatosInvalidosOPasswordGuardadoNulo() {
        FakeUsuarioGateway usuarioGateway = new FakeUsuarioGateway();
        usuarioGateway.usuarioPorEmail = crearUsuario("1234567890", "juan.perez@example.com", null);
        UsuarioUseCase usuarioUseCase = new UsuarioUseCase(usuarioGateway, new FakeSecurityGateway());

        assertFalse(usuarioUseCase.login(null, "Clave123"));
        assertFalse(usuarioUseCase.login("juan.perez@example.com", null));
        assertFalse(usuarioUseCase.login(" ", "Clave123"));
        assertFalse(usuarioUseCase.login("juan.perez@example.com", " "));
        assertFalse(usuarioUseCase.login("juan.perez@example.com", "Clave123"));
    }

    @Test
    void eliminarUsuarioEliminaCuandoExiste() {
        FakeUsuarioGateway usuarioGateway = new FakeUsuarioGateway();
        usuarioGateway.usuarioPorCedula = crearUsuario("1234567890", "juan.perez@example.com", "encrypted:Clave123");
        UsuarioUseCase usuarioUseCase = new UsuarioUseCase(usuarioGateway, new FakeSecurityGateway());

        usuarioUseCase.eliminarUsuario("1234567890");

        assertEquals("1234567890", usuarioGateway.cedulaEliminada);
    }

    @Test
    void eliminarUsuarioFallaCuandoNoExiste() {
        UsuarioUseCase usuarioUseCase = new UsuarioUseCase(new FakeUsuarioGateway(), new FakeSecurityGateway());

        UsuarioNoEncontradoException error = assertThrows(
                UsuarioNoEncontradoException.class,
                () -> usuarioUseCase.eliminarUsuario("1234567890")
        );

        assertEquals("Usuario no encontrado", error.getMessage());
    }

    private Usuario crearUsuario(String cedula, String email, String password) {
        return new Usuario(
                cedula,
                "Juan Perez",
                "3001234567",
                email,
                password,
                28,
                "CLIENTE"
        );
    }

    private static class FakeUsuarioGateway implements UsuarioGateway {
        private Usuario usuarioGuardado;
        private Usuario usuarioPorCedula;
        private Usuario usuarioPorEmail;
        private String cedulaEliminada;

        @Override
        public Usuario guardarUsuario(Usuario usuario) {
            usuarioGuardado = usuario;
            return usuario;
        }

        @Override
        public Usuario buscarUsuario(String cedula) {
            if (usuarioPorCedula == null || !usuarioPorCedula.getCedula().equals(cedula)) {
                return null;
            }
            return usuarioPorCedula;
        }

        @Override
        public Usuario buscarPorEmail(String email) {
            if (usuarioPorEmail == null || !usuarioPorEmail.getEmail().equals(email)) {
                return null;
            }
            return usuarioPorEmail;
        }

        @Override
        public void eliminarUsuario(String cedula) {
            cedulaEliminada = cedula;
        }
    }

    private static class FakeSecurityGateway implements SecurityGateway {
        @Override
        public String encrypt(String rawValue) {
            return "encrypted:" + rawValue;
        }

        @Override
        public boolean matches(String rawValue, String encryptedValue) {
            return encrypt(rawValue).equals(encryptedValue);
        }
    }
}
