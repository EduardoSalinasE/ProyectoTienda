package com.isil.ProyectoTienda.repository;

import com.isil.ProyectoTienda.model.Orden;
import com.isil.ProyectoTienda.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Integer> {

    List<Orden> findByUsuario (Usuario usuario);
}
