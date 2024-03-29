package com.isil.ProyectoTienda.repository;

import com.isil.ProyectoTienda.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByEmail(String email);
    Usuario findByEmailIgnoreCase(String email);
    Usuario findByToken(String token);
}
