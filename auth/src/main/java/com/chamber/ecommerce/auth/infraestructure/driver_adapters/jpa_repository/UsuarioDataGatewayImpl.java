package com.chamber.ecommerce.auth.infraestructure.driver_adapters.jpa_repository;

import com.chamber.ecommerce.auth.domain.model.Usuario;
import com.chamber.ecommerce.auth.domain.model.gateway.UsuarioGateway;
import com.chamber.ecommerce.auth.infraestructure.mapper.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UsuarioDataGatewayImpl implements UsuarioGateway {
    private final UsuarioDataJpaRepository repository;
    private final UsuarioMapper usuarioMapper;

    @Override
    public Usuario guardarUsuario(Usuario usuario) {
        UsuarioData usuarioData = usuarioMapper.toUsuarioData(usuario);
        UsuarioData usuarioDataGuardado = repository.save(usuarioData);
        Usuario usuarioGuardado = usuarioMapper.toUsuario(usuarioDataGuardado);
        return usuarioGuardado;
    }

    @Override
    public Usuario buscarUsuario(String cedula) {
        return repository.findById(cedula).map(usuarioMapper::toUsuario).orElse(null);
    }

    @Override
    public Usuario buscarPorEmail(String email) {
        return repository.findByEmail(email).map(usuarioMapper::toUsuario).orElse(null);
    }

    @Override
    public void eliminarUsuario(String cedula) {
        repository.deleteById(cedula);
    }
}
