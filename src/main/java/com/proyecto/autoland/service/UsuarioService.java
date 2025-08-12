package com.proyecto.autoland.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.proyecto.autoland.model.Usuario;
import com.proyecto.autoland.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void testPassword(String passwordPlain, String hashBd) {
        System.out.println("Password plano: '" + passwordPlain + "'");
        System.out.println("Hash BD: '" + hashBd + "'");
        boolean match = passwordEncoder.matches(passwordPlain, hashBd);
        System.out.println("¿Coincide?: " + match);
    }

    public Usuario registrarUsuario(Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> obtenerPorId(Integer id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Usuario actualizarUsuario(Integer id, Usuario datos) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario con id " + id + " no encontrado");
        }
        datos.setIdUsuario(id);

        if (datos.getContrasena() != null && !datos.getContrasena().isEmpty()) {
            datos.setContrasena(passwordEncoder.encode(datos.getContrasena()));
        }

        return usuarioRepository.save(datos);
    }

    public void eliminarUsuario(Integer id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
        } else {
            throw new RuntimeException("Usuario con id " + id + " no encontrado");
        }
    }
}

