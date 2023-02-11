package com.isil.ProyectoTienda.service;

import com.isil.ProyectoTienda.model.ReportesProveedores;

import java.util.List;

public interface ReporteProveedoresService {

    ReportesProveedores save(ReportesProveedores reportes);

    List<ReportesProveedores> findAll();
}
