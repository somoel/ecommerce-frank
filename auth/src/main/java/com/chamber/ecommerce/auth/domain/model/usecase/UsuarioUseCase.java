package com.chamber.ecommerce.auth.domain.model.usecase;

import com.chamber.ecommerce.auth.domain.model.Usuario;
import com.chamber.ecommerce.auth.domain.model.exception.DatosInvalidosException;
import com.chamber.ecommerce.auth.domain.model.exception.UsuarioNoEncontradoException;
import com.chamber.ecommerce.auth.domain.model.exception.UsuarioYaExisteException;
import com.chamber.ecommerce.auth.domain.model.gateway.SecurityGateway;
import com.chamber.ecommerce.auth.domain.model.gateway.UsuarioGateway;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UsuarioUseCase {
    private final UsuarioGateway usuarioGateway;
    private final SecurityGateway securityGateway;

    public Usuario guardarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new DatosInvalidosException("Usuario no puede ser nulo");
        }
        if (usuario.getEmail() == null || usuario.getPassword() == null || usuario.getEmail().isBlank() || usuario.getPassword().isBlank()) {
            throw new DatosInvalidosException("Email o password no puede ser nulo");
        }
        if (usuario.getCedula() != null && usuarioGateway.buscarUsuario(usuario.getCedula()) != null) {
            throw new UsuarioYaExisteException("El usuario ya existe");
        }
        if (usuarioGateway.buscarPorEmail(usuario.getEmail()) != null) {
            throw new UsuarioYaExisteException("El email ya esta registrado");
        }
        usuario.setPassword(securityGateway.encrypt(usuario.getPassword()));
        return usuarioGateway.guardarUsuario(usuario);
    }

    public Usuario buscarUsuario(String cedula) {
        Usuario usuario = usuarioGateway.buscarUsuario(cedula);
        if (usuario == null) {
            throw new UsuarioNoEncontradoException("Usuario no encontrado");
        }
        return usuario;
    }

    public Usuario login(String email, String password) {
        if (email == null || password == null || email.isBlank() || password.isBlank()) {
            return null;
        }
        Usuario usuario = usuarioGateway.buscarPorEmail(email);
        if (usuario == null || usuario.getPassword() == null) {
            return null;
        }
        if (securityGateway.matches(password, usuario.getPassword())) {
            return usuario;
        }
        return null;
    }

    public void eliminarUsuario(String cedula) {
        if (usuarioGateway.buscarUsuario(cedula) == null) {
            throw new UsuarioNoEncontradoException("Usuario no encontrado");
        }
        usuarioGateway.eliminarUsuario(cedula);
    }
}
