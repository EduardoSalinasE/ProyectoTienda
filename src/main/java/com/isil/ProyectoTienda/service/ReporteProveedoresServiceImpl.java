package com.isil.ProyectoTienda.service;

import com.isil.ProyectoTienda.model.ReportesProveedores;
import com.isil.ProyectoTienda.repository.ReporteProveedoresRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReporteProveedoresServiceImpl implements ReporteProveedoresService{

    @Autowired
    private ReporteProveedoresRepository reporteProveedoresRepository;

    @Override
    public ReportesProveedores save(ReportesProveedores reportes) {
        return reporteProveedoresRepository.save(reportes);
    }

    @Override
    public List<ReportesProveedores> findAll() {
        return reporteProveedoresRepository.findAll();
    }
}
