package com.proyecto.autoland.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.proyecto.autoland.model.Contacto;

@Repository
public interface ContactoRepository extends JpaRepository<Contacto, Integer> {
    
    List<Contacto> findByEstado(String estado);
    
    List<Contacto> findByConsulta(String consulta);
    
    @Query("SELECT c FROM Contacto c WHERE c.dni = :dni ORDER BY c.fechaContacto DESC")
    List<Contacto> findByDniOrderByFechaDesc(@Param("dni") String dni);
    
    @Query("SELECT c FROM Contacto c ORDER BY c.fechaContacto DESC")
    List<Contacto> findAllOrderByFechaDesc();
}