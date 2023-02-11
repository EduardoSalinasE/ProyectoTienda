package com.isil.ProyectoTienda.service;

import com.isil.ProyectoTienda.model.ReportesProductos;

import java.util.List;

public interface ReporteProductosService {

    ReportesProductos save (ReportesProductos reportes);

    List<ReportesProductos> findAll();
}
