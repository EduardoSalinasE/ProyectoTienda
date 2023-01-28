package com.isil.ProyectoTienda.service;

import com.isil.ProyectoTienda.model.Proveedor;

import java.util.List;
import java.util.Optional;

public interface ProveedorService {

    public Proveedor save(Proveedor proveedor);
    public Optional<Proveedor> get(Integer id);
    public void update(Proveedor proveedor);
    public void delete(Integer id);
    public List<Proveedor> findAll();



}
