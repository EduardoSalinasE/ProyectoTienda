package com.isil.ProyectoTienda.service;

import com.isil.ProyectoTienda.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    List<Usuario> findAll();
    Optional<Usuario> findById(Integer id);
    Usuario save (Usuario usuario);
    Optional<Usuario> findByEmail(String email);
    public void update(Usuario usuario);
    Usuario findByToken(String token);
}
