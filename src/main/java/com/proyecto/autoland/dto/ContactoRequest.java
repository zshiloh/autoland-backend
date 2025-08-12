package com.proyecto.autoland.dto;

import lombok.Data;

@Data
public class ContactoRequest {
    private String nombre;
    private String dni;
    private String telefono;
    private String email;
    private String consulta;
    private String mensaje;
}