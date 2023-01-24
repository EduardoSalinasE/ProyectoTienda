package com.isil.ProyectoTienda.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "proveedor")
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre;
    private String boleta;
    private double precio;
    private int cantidad;

    @ManyToOne
    private Producto producto;

    @ManyToOne
    private Usuario usuario;

    @Override
    public String toString() {
        return "Proveedor [id=" + id + ", nombre=" + nombre + ", boleta=" + boleta + ", precio=" + precio + ", cantidad=" + cantidad + "]";
    }
}
