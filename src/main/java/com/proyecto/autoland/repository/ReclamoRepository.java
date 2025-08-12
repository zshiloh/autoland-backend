package com.proyecto.autoland.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.proyecto.autoland.model.Reclamo;

@Repository
public interface ReclamoRepository extends JpaRepository<Reclamo, Integer> {
    
    Optional<Reclamo> findByNumeroReclamo(String numeroReclamo);
    
    List<Reclamo> findByEstado(String estado);
    
    List<Reclamo> findByTipo(String tipo);
    
    @Query("SELECT r FROM Reclamo r WHERE r.dni = :dni ORDER BY r.fechaReclamo DESC")
    List<Reclamo> findByDniOrderByFechaDesc(@Param("dni") String dni);
    
    @Query("SELECT r FROM Reclamo r ORDER BY r.fechaReclamo DESC")
    List<Reclamo> findAllOrderByFechaDesc();
}