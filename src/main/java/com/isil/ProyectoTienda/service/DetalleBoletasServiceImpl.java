package com.isil.ProyectoTienda.service;

import com.isil.ProyectoTienda.model.DetalleBoletas;
import com.isil.ProyectoTienda.repository.DetalleBoletasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DetalleBoletasServiceImpl implements DetalleBoletasService {

    @Autowired
    private DetalleBoletasRepository detalleBoletasRepository;

    @Override
    public DetalleBoletas save(DetalleBoletas detalleBoletas) {
        return detalleBoletasRepository.save(detalleBoletas);
    }

    @Override
    public List<DetalleBoletas> findAllByProveedor_id(Integer id) {
        return detalleBoletasRepository.findAllByProveedor_id(id);
    }

    @Override
    public List<DetalleBoletas> findAll() {
        return detalleBoletasRepository.findAll();
    }
}
