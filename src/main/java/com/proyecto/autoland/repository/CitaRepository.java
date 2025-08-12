package com.proyecto.autoland.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.proyecto.autoland.model.Cita;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Integer> {
    
    @Query("SELECT c FROM Cita c WHERE c.usuario.idUsuario = :usuarioId ORDER BY c.fecha_creacion DESC")
    List<Cita> findByUsuarioIdOrderByFechaCreacionDesc(@Param("usuarioId") Integer usuarioId);
    
    List<Cita> findByEstado(String estado);
    
    List<Cita> findBySucursal(String sucursal);
    
    List<Cita> findByFecha(String fecha);
    
    @Query("SELECT c FROM Cita c WHERE c.usuario.idUsuario = :usuarioId AND c.estado = :estado")
    List<Cita> findByUsuarioIdAndEstado(@Param("usuarioId") Integer usuarioId, @Param("estado") String estado);
    
    @Query("SELECT COUNT(c) FROM Cita c WHERE c.fecha = :fecha AND c.horario = :horario AND c.sucursal = :sucursal")
    Long contarCitasEnHorario(@Param("fecha") String fecha, @Param("horario") String horario, @Param("sucursal") String sucursal);
    
    @Query("SELECT COUNT(c) FROM Cita c WHERE c.usuario.idUsuario = :usuarioId AND c.estado != 'cancelada'")
    Long contarCitasActivasPorUsuario(@Param("usuarioId") Integer usuarioId);
    
    @Query("SELECT c FROM Cita c WHERE c.usuario.idUsuario = :usuarioId AND c.estado != 'cancelada' ORDER BY c.fecha_creacion DESC")
    List<Cita> findByUsuarioIdAndEstadoNotCancelada(@Param("usuarioId") Integer usuarioId);
}