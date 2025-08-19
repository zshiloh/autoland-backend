package com.proyecto.autoland.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.autoland.dto.ContactoRequest;
import com.proyecto.autoland.model.Contacto;
import com.proyecto.autoland.repository.ContactoRepository;

@Service
public class ContactoService {

    @Autowired
    private ContactoRepository contactoRepository;

    public Contacto crearContacto(ContactoRequest request) {
        Contacto contacto = new Contacto();
        contacto.setNombre(request.getNombre());
        contacto.setDni(request.getDni());
        contacto.setTelefono(request.getTelefono());
        contacto.setEmail(request.getEmail());
        contacto.setConsulta(request.getConsulta());
        contacto.setMensaje(request.getMensaje());
        
        return contactoRepository.save(contacto);
    }

    public List<Contacto> obtenerTodos() {
        return contactoRepository.findAllOrderByFechaDesc();
    }

    public Optional<Contacto> obtenerPorId(Integer id) {
        return contactoRepository.findById(id);
    }

    public List<Contacto> obtenerPorDni(String dni) {
        return contactoRepository.findByDniOrderByFechaDesc(dni);
    }

    public List<Contacto> obtenerPorEstado(String estado) {
        return contactoRepository.findByEstado(estado);
    }

    public Contacto actualizarEstado(Integer id, String nuevoEstado) {
        Optional<Contacto> contactoOpt = contactoRepository.findById(id);
        if (contactoOpt.isEmpty()) {
            throw new RuntimeException("Contacto no encontrado");
        }
        
        Contacto contacto = contactoOpt.get();
        contacto.setEstado(nuevoEstado);
        
        return contactoRepository.save(contacto);
    }
}