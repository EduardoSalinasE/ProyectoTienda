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
    private String ruc;
    private String direccion;

    @ManyToOne
    private Usuario usuario;

    @Override
    public String toString() {
        return "Proveedor [id=" + id + ", nombre=" + nombre + ", ruc="+ ruc +",direccion=" + direccion + "]";
    }
}
