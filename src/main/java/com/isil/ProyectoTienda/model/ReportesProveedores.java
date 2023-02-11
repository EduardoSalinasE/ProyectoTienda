package com.isil.ProyectoTienda.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "reports_prov")
public class ReportesProveedores {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombreProveedor;
    private String ruc;
    private Date fechaCreacion;
    private Date fechaModificacion;
    private String tipoOperacion;

    @ManyToOne
    private Usuario usuario;

}
