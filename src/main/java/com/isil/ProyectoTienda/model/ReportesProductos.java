package com.isil.ProyectoTienda.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "reports_pro")
public class ReportesProductos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombreProducto;
    private float cantidad;
    private float precio;
    private Date fechaCreacion;
    private Date fechaModificacion;
    private String tipoOperacion;

    @ManyToOne
    private Usuario usuario;

}
