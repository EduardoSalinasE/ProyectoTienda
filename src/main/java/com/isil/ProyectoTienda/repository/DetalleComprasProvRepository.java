package com.isil.ProyectoTienda.repository;

import com.isil.ProyectoTienda.model.DetalleComprasProv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleComprasProvRepository extends JpaRepository<DetalleComprasProv, Integer> {

}
