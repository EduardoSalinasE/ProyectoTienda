package com.isil.ProyectoTienda.service;

import com.isil.ProyectoTienda.model.DetalleBoletas;

import java.util.List;
import java.util.Optional;

public interface DetalleBoletasService {

    DetalleBoletas save (DetalleBoletas detalleBoletas);

    public List<DetalleBoletas> findAllByProveedor_id(Integer id);

    public List<DetalleBoletas> findAll();
}
