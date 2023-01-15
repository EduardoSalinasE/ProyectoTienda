package com.isil.ProyectoTienda.service;

import com.isil.ProyectoTienda.model.Orden;
import com.isil.ProyectoTienda.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface OrdenService {
    List<Orden> findAll();
    Optional<Orden> findById(Integer id);
    Orden save (Orden orden);
    String generarNumeroOrden();
    List<Orden> findByUsuario (Usuario usuario);
}
