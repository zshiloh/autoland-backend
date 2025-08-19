package com.proyecto.autoland.controller;

import com.proyecto.autoland.dto.UsuarioDTO;
import com.proyecto.autoland.model.Usuario;
import com.proyecto.autoland.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    @PostMapping
    public ResponseEntity<UsuarioDTO> registrar(@RequestBody Usuario usuario) {
        Usuario registrado = usuarioService.registrarUsuario(usuario);
        return ResponseEntity.ok(toDTO(registrado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> obtenerPorId(@PathVariable Integer id) {
        return usuarioService.obtenerPorId(id)
                .map(u -> ResponseEntity.ok(toDTO(u)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioDTO> obtenerPorEmail(@PathVariable String email) {
        return usuarioService.obtenerPorEmail(email)
                .map(u -> ResponseEntity.ok(toDTO(u)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> actualizar(@PathVariable Integer id, @RequestBody Usuario usuario) {
        Usuario actualizado = usuarioService.actualizarUsuario(id, usuario);
        return ResponseEntity.ok(toDTO(actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.ok().build();
    }
}