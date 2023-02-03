package com.isil.ProyectoTienda.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "detalleCompras")
public class DetalleComprasProv {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Date fechaCreacion;
    private String boleta;
    private double precio;
    private String usuarioRegistro;
    private String usuarioModificacion;
    private Date fechaModificacion;

    @ManyToOne
    private Proveedor proveedor;

    @Override
    public String toString() {
        return "detalleBoleta [id=" + id + ", fechaCreacion=" + fechaCreacion + ", boleta=" + boleta + ",precio=" + precio
                + "]";
    }
}
