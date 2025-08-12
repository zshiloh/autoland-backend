package com.proyecto.autoland.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.autoland.dto.ReclamosRequest;
import com.proyecto.autoland.model.Reclamo;
import com.proyecto.autoland.repository.ReclamoRepository;

@Service
public class ReclamoService {

    @Autowired
    private ReclamoRepository reclamoRepository;

    private String generarNumeroReclamo() {
        return "REC-" + System.currentTimeMillis();
    }

    public Reclamo crearReclamo(ReclamosRequest request) {
        Reclamo reclamo = new Reclamo();
        
        reclamo.setNombres(request.getNombres());
        reclamo.setApellidos(request.getApellidos());
        reclamo.setDni(request.getDni());
        reclamo.setTelefono(request.getTelefono());
        reclamo.setEmail(request.getEmail());
        reclamo.setCiudad(request.getCiudad());
        reclamo.setPlaca(request.getPlaca());
        reclamo.setDetalle(request.getDetalle());
        
        reclamo.setTipo(request.getTipo());
        reclamo.setServicio(request.getServicio());
        reclamo.setMotivo(request.getMotivo());
        reclamo.setLocal(request.getLocal());
        
        reclamo.setNumeroReclamo(generarNumeroReclamo());
        
        return reclamoRepository.save(reclamo);
    }

    public List<Reclamo> obtenerTodos() {
        return reclamoRepository.findAllOrderByFechaDesc();
    }

    public Optional<Reclamo> obtenerPorId(Integer id) {
        return reclamoRepository.findById(id);
    }

    public Optional<Reclamo> obtenerPorNumero(String numeroReclamo) {
        return reclamoRepository.findByNumeroReclamo(numeroReclamo);
    }

    public List<Reclamo> obtenerPorDni(String dni) {
        return reclamoRepository.findByDniOrderByFechaDesc(dni);
    }

    public List<Reclamo> obtenerPorEstado(String estado) {
        return reclamoRepository.findByEstado(estado);
    }

    public Reclamo actualizarEstado(Integer id, String nuevoEstado) {
        Optional<Reclamo> reclamoOpt = reclamoRepository.findById(id);
        if (reclamoOpt.isEmpty()) {
            throw new RuntimeException("Reclamo no encontrado");
        }
        
        Reclamo reclamo = reclamoOpt.get();
        reclamo.setEstado(nuevoEstado);
        
        return reclamoRepository.save(reclamo);
    }
}