package com.proyecto.autoland.dto;

import lombok.*;

@Data
public class CrearCitaRequest {

    private String placa;
    private String marca;
    private String modelo;
    private String anio;
    
    private String servicio;
    private String sucursal;
    
    private String fecha;
    private String horario;
    
    private String observaciones;
}