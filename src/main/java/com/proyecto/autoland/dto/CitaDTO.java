package com.proyecto.autoland.dto;

import java.time.LocalDateTime;
import lombok.*;

@Data
public class CitaDTO {
    private Integer id_cita;
    
    private String placa;
    private String marca;
    private String modelo;
    private String anio;
    
    private String servicio;
    private String sucursal;
    
    private String fecha;
    private String horario;
    private String estado;
    
    private Integer usuarioId;
    private String usuario_nombre;
    private String usuario_email;
    
    private LocalDateTime fecha_creacion;
    private String observaciones;
}