package com.proyecto.autoland.model;

import java.time.LocalDateTime;
import lombok.*;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "citas")
public class Cita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_cita;
    
    private String placa;
    private String marca;
    private String modelo;
    private String anio;
    
    private String servicio;
    private String sucursal;
    
    private String fecha;
    private String horario;
    private String estado = "pendiente";
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    
    @Column(columnDefinition = "DATETIME")
    private LocalDateTime fecha_creacion = LocalDateTime.now();
    
    private String observaciones;
}