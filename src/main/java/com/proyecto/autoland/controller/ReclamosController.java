package com.proyecto.autoland.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.proyecto.autoland.dto.ReclamosRequest;
import com.proyecto.autoland.model.Reclamo;
import com.proyecto.autoland.service.ReclamoService;

@RestController
@RequestMapping("/api/reclamos")
@CrossOrigin(origins = "*")
public class ReclamosController {

    @Autowired
    private ReclamoService reclamoService;

    @PostMapping
    public ResponseEntity<?> enviarReclamo(@RequestBody ReclamosRequest request) {
        try {
            System.out.println("=== RECLAMOS REQUEST ===");
            System.out.println("Datos recibidos: " + request);
            
            if (request.getNombres() == null || request.getNombres().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Los nombres son requeridos");
            }
            
            if (request.getApellidos() == null || request.getApellidos().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Los apellidos son requeridos");
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
            
            if (request.getCiudad() == null || request.getCiudad().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("La ciudad es requerida");
            }
            
            if (request.getDetalle() == null || request.getDetalle().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("El detalle del reclamo es requerido");
            }
            
            if (request.getTipo() == null || request.getTipo().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("El tipo de solicitud es requerido");
            }
            
            if (request.getServicio() == null || request.getServicio().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("El tipo de servicio es requerido");
            }
            
            if (request.getMotivo() == null || request.getMotivo().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("El motivo de contacto es requerido");
            }
            
            if (request.getLocal() == null || request.getLocal().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("El local es requerido");
            }
            
            if (!request.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                return ResponseEntity.badRequest().body("Formato de email inválido");
            }
            
            if (!request.getDni().matches("\\d{8}")) {
                return ResponseEntity.badRequest().body("El DNI debe tener 8 dígitos");
            }
            
            Reclamo reclamoGuardado = reclamoService.crearReclamo(request);
            
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Reclamo registrado y guardado exitosamente");
            respuesta.put("id_reclamo", reclamoGuardado.getIdReclamo());
            respuesta.put("numero_reclamo", reclamoGuardado.getNumeroReclamo());
            respuesta.put("fecha", reclamoGuardado.getFechaReclamo());
            respuesta.put("tipo", reclamoGuardado.getTipo());
            respuesta.put("estado", reclamoGuardado.getEstado());
            
            System.out.println("✅ Reclamo guardado con número: " + reclamoGuardado.getNumeroReclamo());
            
            return ResponseEntity.ok(respuesta);
            
        } catch (Exception e) {
            System.err.println("❌ Error al procesar reclamo: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor");
        }
    }

    @GetMapping
    public ResponseEntity<List<Reclamo>> obtenerTodos() {
        List<Reclamo> reclamos = reclamoService.obtenerTodos();
        return ResponseEntity.ok(reclamos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        return reclamoService.obtenerPorId(id)
                .map(reclamo -> ResponseEntity.ok(reclamo))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/numero/{numeroReclamo}")
    public ResponseEntity<?> obtenerPorNumero(@PathVariable String numeroReclamo) {
        return reclamoService.obtenerPorNumero(numeroReclamo)
                .map(reclamo -> ResponseEntity.ok(reclamo))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/dni/{dni}")
    public ResponseEntity<List<Reclamo>> obtenerPorDni(@PathVariable String dni) {
        List<Reclamo> reclamos = reclamoService.obtenerPorDni(dni);
        return ResponseEntity.ok(reclamos);
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
            
            Reclamo reclamoActualizado = reclamoService.actualizarEstado(id, nuevoEstado);
            return ResponseEntity.ok(reclamoActualizado);
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}