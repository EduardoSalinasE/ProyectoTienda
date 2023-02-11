package com.isil.ProyectoTienda.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre;
    private String username;
    private String email;
    private String direccion;
    private String telefono;
    private String tipo;
    private String token;
    private String password;

    private int eneable;

    @OneToMany(mappedBy = "usuario")
    private List<Producto> productos;

    @Override
    public String toString() {
        return "Usuario [id=" + id + ", nombre=" + nombre + ", email=" + email
                + ", direccion=" + direccion + ", tipo=" + tipo + "]";
    }
}
