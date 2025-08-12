package com.proyecto.autoland.model;

import java.time.LocalDateTime;
import lombok.*;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;
    
    private String nombre;
    private String apellidos;

    @Column(unique = true)
    private String email;

    private String contrasena;

    @Column(unique = true)
    private String dni;
    private String telefono;

    @Column(name = "fecha_registro", columnDefinition = "DATETIME")
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    private String rol = "cliente";
}