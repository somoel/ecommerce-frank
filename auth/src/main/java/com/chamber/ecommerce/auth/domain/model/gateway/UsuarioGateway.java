package com.chamber.ecommerce.auth.domain.model.gateway;

import com.chamber.ecommerce.auth.domain.model.Usuario;

public interface UsuarioGateway {
    Usuario guardarUsuario(Usuario usuario);

    Usuario buscarUsuario(String cedula);

    Usuario buscarPorEmail(String email);

    void eliminarUsuario(String cedula);
}
