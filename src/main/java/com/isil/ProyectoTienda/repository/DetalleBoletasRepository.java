package com.isil.ProyectoTienda.repository;

import com.isil.ProyectoTienda.model.DetalleBoletas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleBoletasRepository extends JpaRepository<DetalleBoletas, Integer> {

    List<DetalleBoletas> findAllByProveedor_id(Integer id);
}
