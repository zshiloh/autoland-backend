package com.proyecto.autoland.model;

import java.time.LocalDateTime;
import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "contactos")
public class Contacto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contacto")
    private Integer idContacto;
    
    private String nombre;
    private String dni;
    private String telefono;
    private String email;
    private String consulta;
    private String mensaje;
    
    @Column(name = "fecha_contacto", columnDefinition = "DATETIME")
    private LocalDateTime fechaContacto = LocalDateTime.now();
    
    private String estado = "pendiente";
}