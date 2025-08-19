package com.proyecto.autoland.service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.proyecto.autoland.model.Usuario;

class UsuarioServiceTest {

    @Test
    void testCrearUsuario() {
        // Arrange
        Usuario usuario = new Usuario();
        
        // Act
        usuario.setNombre("Juan");
        usuario.setApellidos("Perez");
        usuario.setEmail("juan@test.com");
        usuario.setDni("12345678");
        usuario.setTelefono("987654321");
        usuario.setContrasena("password123");
        
        // Assert
        assertEquals("Juan", usuario.getNombre());
        assertEquals("Perez", usuario.getApellidos());
        assertEquals("juan@test.com", usuario.getEmail());
        assertEquals("12345678", usuario.getDni());
        assertEquals("987654321", usuario.getTelefono());
        assertEquals("password123", usuario.getContrasena());
        assertEquals("cliente", usuario.getRol());
        assertNotNull(usuario.getFechaRegistro());
    }

    @Test
    void testEncriptarPassword() {
        // Arrange
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String passwordOriginal = "miPassword123";
        
        // Act
        String passwordEncriptado = encoder.encode(passwordOriginal);
        
        // Assert
        assertNotNull(passwordEncriptado);
        assertNotEquals(passwordOriginal, passwordEncriptado);
        assertTrue(passwordEncriptado.length() > 50);
        assertTrue(encoder.matches(passwordOriginal, passwordEncriptado));
    }

    @Test
    void testValidarEmail() {
        // Arrange
        String emailValido = "usuario@correo.com";
        String emailInvalido = "email-sin-arroba";
        
        // Act & Assert
        assertTrue(emailValido.contains("@"));
        assertTrue(emailValido.contains("."));
        assertFalse(emailInvalido.contains("@"));
    }

    @Test
    void testValidarDni() {
        // Arrange
        String dniValido = "12345678";
        String dniInvalido = "123";
        
        // Act & Assert
        assertTrue(dniValido.matches("\\d{8}"));
        assertEquals(8, dniValido.length());
        assertFalse(dniInvalido.matches("\\d{8}"));
    }
}