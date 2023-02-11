package com.isil.ProyectoTienda.repository;

import com.isil.ProyectoTienda.model.DetalleBoletas;
import com.isil.ProyectoTienda.model.Proveedor;
import org.hibernate.sql.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DetalleBoletasRepository extends JpaRepository<DetalleBoletas, Integer> {

    List<DetalleBoletas> findAllByProveedor_id(Integer id);

    Optional<DetalleBoletas> findByNombreProductoAndProveedor_id(String nombre, Integer id);
}
