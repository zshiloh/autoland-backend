package com.proyecto.autoland.dto;

import lombok.*;

@Data
public class UsuarioDTO {
    private Integer idUsuario;
    private String nombre;
    private String apellidos;
    private String email;
    private String dni;
    private String telefono;
    private String rol;
}