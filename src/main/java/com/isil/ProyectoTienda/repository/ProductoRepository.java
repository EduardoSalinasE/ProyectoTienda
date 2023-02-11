package com.isil.ProyectoTienda.repository;

import com.isil.ProyectoTienda.model.DetalleBoletas;
import com.isil.ProyectoTienda.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
}
