package com.proyecto.autoland.model;

import java.time.LocalDateTime;
import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "reclamos")
public class Reclamo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reclamo")
    private Integer idReclamo;
    
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
    
    @Column(name = "numero_reclamo", unique = true)
    private String numeroReclamo;
    
    @Column(name = "fecha_reclamo", columnDefinition = "DATETIME")
    private LocalDateTime fechaReclamo = LocalDateTime.now();
    
    private String estado = "recibido";
}