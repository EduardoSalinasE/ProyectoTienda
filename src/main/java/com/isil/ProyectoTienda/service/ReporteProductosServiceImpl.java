package com.isil.ProyectoTienda.service;

import com.isil.ProyectoTienda.model.ReportesProductos;
import com.isil.ProyectoTienda.repository.ReporteProductosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReporteProductosServiceImpl implements ReporteProductosService {

    @Autowired
    private ReporteProductosRepository reporteRepository;
    @Override
    public ReportesProductos save(ReportesProductos reportes) {
        return reporteRepository.save(reportes);
    }

    @Override
    public List<ReportesProductos> findAll() {
        return reporteRepository.findAll();
    }

}
