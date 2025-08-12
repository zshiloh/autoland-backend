package com.proyecto.autoland.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.proyecto.autoland.dto.CitaDTO;
import com.proyecto.autoland.dto.CrearCitaRequest;
import com.proyecto.autoland.security.JwtUtil;
import com.proyecto.autoland.service.CitaService;
import com.proyecto.autoland.service.UsuarioService;

@RestController
@RequestMapping("/api/citas")
@CrossOrigin(origins = "*")
public class CitaController {

    @Autowired
    private CitaService citaService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private JwtUtil jwtUtil;

    private Integer obtenerUsuarioIdDesdeToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Token no válido");
        }
        
        String token = authHeader.substring(7);
        
        if (!jwtUtil.validarToken(token)) {
            throw new RuntimeException("Token expirado o inválido");
        }
        
        String email = jwtUtil.extraerEmail(token);
        return usuarioService.obtenerPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"))
                .getIdUsuario();
    }

    @PostMapping
    public ResponseEntity<?> crearCita(
            @RequestBody CrearCitaRequest request,
            @RequestHeader("Authorization") String authHeader) {
        try {
            System.out.println("=== CREAR CITA REQUEST ===");
            System.out.println("Datos recibidos: " + request);
            
            Integer usuarioId = obtenerUsuarioIdDesdeToken(authHeader);
            CitaDTO citaCreada = citaService.crearCita(request, usuarioId);
            
            return ResponseEntity.ok(citaCreada);
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor");
        }
    }
    
    @PutMapping("/{citaId}")
    public ResponseEntity<?> actualizarCita(
            @PathVariable Integer citaId,
            @RequestBody CrearCitaRequest request,
            @RequestHeader("Authorization") String authHeader) {
        try {
            System.out.println("=== ACTUALIZAR CITA REQUEST ===");
            System.out.println("ID Cita: " + citaId);
            System.out.println("Datos recibidos: " + request);
            
            Integer usuarioId = obtenerUsuarioIdDesdeToken(authHeader);
            CitaDTO citaActualizada = citaService.actualizarCita(citaId, request, usuarioId);
            
            return ResponseEntity.ok(citaActualizada);
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor");
        }
    }

    @GetMapping("/mis-citas")
    public ResponseEntity<?> obtenerMisCitas(@RequestHeader("Authorization") String authHeader) {
        try {
            Integer usuarioId = obtenerUsuarioIdDesdeToken(authHeader);
            List<CitaDTO> citas = citaService.obtenerCitasPorUsuario(usuarioId);
            
            return ResponseEntity.ok(citas);
            
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor");
        }
    }

    @GetMapping("/{citaId}")
    public ResponseEntity<?> obtenerCitaPorId(@PathVariable Integer citaId) {
        try {
            return citaService.obtenerCitaPorId(citaId)
                    .map(cita -> ResponseEntity.ok(cita))
                    .orElse(ResponseEntity.notFound().build());
                    
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor");
        }
    }

    @PutMapping("/{citaId}/estado")
    public ResponseEntity<?> actualizarEstado(
            @PathVariable Integer citaId,
            @RequestBody Map<String, String> datos,
            @RequestHeader("Authorization") String authHeader) {
        try {
            obtenerUsuarioIdDesdeToken(authHeader);
            
            String nuevoEstado = datos.get("estado");
            if (nuevoEstado == null) {
                return ResponseEntity.badRequest().body("Estado requerido");
            }
            
            CitaDTO citaActualizada = citaService.actualizarEstadoCita(citaId, nuevoEstado);
            return ResponseEntity.ok(citaActualizada);
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor");
        }
    }

    @DeleteMapping("/{citaId}")
    public ResponseEntity<?> cancelarCita(
            @PathVariable Integer citaId,
            @RequestHeader("Authorization") String authHeader) {
        try {
            Integer usuarioId = obtenerUsuarioIdDesdeToken(authHeader);
            citaService.cancelarCita(citaId, usuarioId);
            
            return ResponseEntity.ok().body("Cita cancelada exitosamente");
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor");
        }
    }
}