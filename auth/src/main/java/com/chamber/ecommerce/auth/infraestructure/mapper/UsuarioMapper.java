package com.chamber.ecommerce.auth.infraestructure.mapper;

import com.chamber.ecommerce.auth.domain.model.Usuario;
import com.chamber.ecommerce.auth.infraestructure.driver_adapters.jpa_repository.UsuarioData;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {
    public UsuarioData toUsuarioData(Usuario usuario) {
        return new UsuarioData(
                usuario.getCedula(),
                usuario.getNombre(),
                usuario.getTelefono(),
                usuario.getEmail(),
                usuario.getPassword(),
                usuario.getEdad(),
                usuario.getRol()
        );
    }

    public Usuario toUsuario(UsuarioData usuarioData) {
        return new Usuario(
                usuarioData.getCedula(),
                usuarioData.getNombre(),
                usuarioData.getTelefono(),
                usuarioData.getEmail(),
                usuarioData.getPassword(),
                usuarioData.getEdad(),
                usuarioData.getRol()
        );
    }
}
