package com.proyecto.autoland.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.proyecto.autoland.dto.ContactoRequest;
import com.proyecto.autoland.model.Contacto;
import com.proyecto.autoland.service.ContactoService;

@RestController
@RequestMapping("/api/contacto")
@CrossOrigin(origins = "*")
public class ContactoController {

    @Autowired
    private ContactoService contactoService;

    @PostMapping
    public ResponseEntity<?> enviarContacto(@RequestBody ContactoRequest request) {
        try {
            System.out.println("=== CONTACTO REQUEST ===");
            System.out.println("Datos recibidos: " + request);
            
            if (request.getNombre() == null || request.getNombre().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("El nombre es requerido");
            }
            
            if (request.getDni() == null || request.getDni().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("El DNI es requerido");
            }
            
            if (request.getTelefono() == null || request.getTelefono().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("El teléfono es requerido");
            }
            
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("El email es requerido");
            }
            
            if (request.getConsulta() == null || request.getConsulta().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("La consulta es requerida");
            }
            
            if (request.getMensaje() == null || request.getMensaje().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("El mensaje es requerido");
            }
            
            if (!request.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                return ResponseEntity.badRequest().body("Formato de email inválido");
            }
            
            if (!request.getDni().matches("\\d{8}")) {
                return ResponseEntity.badRequest().body("El DNI debe tener 8 dígitos");
            }
            
            Contacto contactoGuardado = contactoService.crearContacto(request);
            
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Contacto enviado y guardado exitosamente");
            respuesta.put("id_contacto", contactoGuardado.getIdContacto());
            respuesta.put("fecha", contactoGuardado.getFechaContacto());
            respuesta.put("estado", contactoGuardado.getEstado());
            
            System.out.println("✅ Contacto guardado con ID: " + contactoGuardado.getIdContacto());
            
            return ResponseEntity.ok(respuesta);
            
        } catch (Exception e) {
            System.err.println("❌ Error al procesar contacto: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor");
        }
    }

    @GetMapping
    public ResponseEntity<List<Contacto>> obtenerTodos() {
        List<Contacto> contactos = contactoService.obtenerTodos();
        return ResponseEntity.ok(contactos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        return contactoService.obtenerPorId(id)
                .map(contacto -> ResponseEntity.ok(contacto))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/dni/{dni}")
    public ResponseEntity<List<Contacto>> obtenerPorDni(@PathVariable String dni) {
        List<Contacto> contactos = contactoService.obtenerPorDni(dni);
        return ResponseEntity.ok(contactos);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(
            @PathVariable Integer id,
            @RequestBody Map<String, String> datos) {
        try {
            String nuevoEstado = datos.get("estado");
            if (nuevoEstado == null) {
                return ResponseEntity.badRequest().body("Estado requerido");
            }
            
            Contacto contactoActualizado = contactoService.actualizarEstado(id, nuevoEstado);
            return ResponseEntity.ok(contactoActualizado);
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}