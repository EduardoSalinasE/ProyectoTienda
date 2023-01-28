package com.isil.ProyectoTienda.service;

import com.isil.ProyectoTienda.model.Proveedor;
import com.isil.ProyectoTienda.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProveedorServiceImpl implements ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Override
    public Proveedor save(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    @Override
    public Optional<Proveedor> get(Integer id) {
        return proveedorRepository.findById(id);
    }

    @Override
    public void update(Proveedor proveedor) {
        proveedorRepository.save(proveedor);
    }

    @Override
    public void delete(Integer id) {
        proveedorRepository.deleteById(id);
    }

    @Override
    public List<Proveedor> findAll() {
        return proveedorRepository.findAll();
    }


}
