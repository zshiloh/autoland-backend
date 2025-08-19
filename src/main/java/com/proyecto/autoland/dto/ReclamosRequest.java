package com.proyecto.autoland.dto;

import lombok.Data;

@Data
public class ReclamosRequest {

    private String nombres;
    private String apellidos;
    private String dni;
    private String telefono;
    private String email;
    private String ciudad;
    private String placa;
    private String detalle;
    
    private String tipo;
    private String servicio;
    private String motivo;
    private String local;
}