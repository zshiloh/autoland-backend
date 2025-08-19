package com.proyecto.autoland.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.proyecto.autoland.dto.UsuarioDTO;
import com.proyecto.autoland.model.Usuario;
import com.proyecto.autoland.security.JwtUtil;
import com.proyecto.autoland.service.UsuarioService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    private UsuarioDTO toDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNombre(usuario.getNombre());
        dto.setApellidos(usuario.getApellidos());
        dto.setEmail(usuario.getEmail());
        dto.setDni(usuario.getDni());
        dto.setTelefono(usuario.getTelefono());
        dto.setRol(usuario.getRol());
        return dto;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registrar(@RequestBody Map<String, String> datos) {
        try {
            System.out.println("=== REGISTRO REQUEST ===");
            System.out.println("Datos recibidos: " + datos);

            String nombre = datos.get("nombre");
            String apellidos = datos.get("apellidos");
            String correo = datos.get("correo");
            String telefono = datos.get("telefono");
            String dni = datos.get("dni");
            String password = datos.get("password");

            if (nombre == null || correo == null || dni == null || password == null) {
                return ResponseEntity.badRequest().body("Faltan campos requeridos");
            }

            Usuario usuario = new Usuario();
            usuario.setNombre(nombre);
            usuario.setApellidos(apellidos);
            usuario.setEmail(correo);
            usuario.setTelefono(telefono);
            usuario.setDni(dni);
            usuario.setContrasena(password);

            Usuario registrado = usuarioService.registrarUsuario(usuario);
            
            return ResponseEntity.ok(toDTO(registrado));
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> datos) {
        try {
            String email = datos.get("email");
            String password = datos.get("password");

            System.out.println("=== LOGIN REQUEST ===");
            System.out.println("Email recibido: '" + email + "'");

            if (email == null || password == null) {
                return ResponseEntity.badRequest().body("Email o contraseña faltantes");
            }

            Optional<Usuario> usuarioOpt = usuarioService.obtenerPorEmail(email.trim());
            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
            }

            Usuario usuario = usuarioOpt.get();
            
            boolean coincide = passwordEncoder.matches(password.trim(), usuario.getContrasena());
            if (!coincide) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
            }

            String token = jwtUtil.generarToken(usuario);

            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("token", token);
            respuesta.put("usuario", toDTO(usuario));

            return ResponseEntity.ok(respuesta);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> obtenerUsuarioActual(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no válido");
            }

            String token = authHeader.substring(7);
            
            if (!jwtUtil.validarToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expirado o inválido");
            }

            String email = jwtUtil.extraerEmail(token);
            Optional<Usuario> usuarioOpt = usuarioService.obtenerPorEmail(email);
            
            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado");
            }

            return ResponseEntity.ok(toDTO(usuarioOpt.get()));
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }
    }
}