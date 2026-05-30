package com.chamber.ecommerce.auth.infraestructure.entry_points;

import com.chamber.ecommerce.auth.domain.model.Usuario;
import com.chamber.ecommerce.auth.domain.model.gateway.TokenGateway;
import com.chamber.ecommerce.auth.domain.model.usecase.UsuarioUseCase;
import com.chamber.ecommerce.auth.infraestructure.driver_adapters.jpa_repository.UsuarioData;
import com.chamber.ecommerce.auth.infraestructure.entry_points.dto.LoginRequest;
import com.chamber.ecommerce.auth.infraestructure.entry_points.dto.LoginResponse;
import com.chamber.ecommerce.auth.infraestructure.mapper.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/ecommerce/usuario")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioUseCase usuarioUseCase;
    private final UsuarioMapper usuarioMapper;
    private final TokenGateway tokenGateway;

    @PostMapping("/save")
    public ResponseEntity<Usuario> saveUsuario(@RequestBody UsuarioData usuarioData) {
        Usuario usuario = usuarioMapper.toUsuario(usuarioData);
        Usuario usuarioValidadoGuardado = usuarioUseCase.guardarUsuario(usuario);
        return new ResponseEntity<>(usuarioValidadoGuardado,HttpStatus.OK);
    }

    @GetMapping("/{cedula}")
    public ResponseEntity<Usuario> buscarById(@PathVariable String cedula) {
        Usuario usuario = usuarioUseCase.buscarUsuario(cedula);
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Usuario usuario = usuarioUseCase.login(loginRequest.getEmail(), loginRequest.getPassword());
        if (usuario != null) {
            String token = tokenGateway.generateToken(usuario);
            LoginResponse response = new LoginResponse(token, usuario.getEmail(), usuario.getCedula(), usuario.getRol());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        return new ResponseEntity<>("Credenciales invalidas", HttpStatus.UNAUTHORIZED);
    }

    @DeleteMapping("/{cedula}")
    public ResponseEntity<String> deleteById(@PathVariable String cedula) {
        usuarioUseCase.eliminarUsuario(cedula);
        return new ResponseEntity<>("Usuario eliminado", HttpStatus.OK);
    }
}
