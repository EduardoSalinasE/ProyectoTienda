package com.isil.ProyectoTienda.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "detalleBoletas")
public class DetalleBoletas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombreProducto;
    private Integer cantidad;
    private float total;

    @ManyToOne
    private Proveedor proveedor;


    @Override
    public String toString() {
        return "DetalleBoletasService [id=" + id + ", nombreProducto=" + nombreProducto + ", cantidad=" + cantidad + ", total=" + total
                + "]";
    }
}
