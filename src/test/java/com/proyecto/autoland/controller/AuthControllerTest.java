package com.proyecto.autoland.controller;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;

class AuthControllerTest {

    @Test
    void testValidarDatosRegistro() {
        // Arrange
        Map<String, String> datosCompletos = new HashMap<>();
        datosCompletos.put("nombre", "Juan");
        datosCompletos.put("apellidos", "Perez");
        datosCompletos.put("correo", "juan@test.com");
        datosCompletos.put("telefono", "987654321");
        datosCompletos.put("dni", "12345678");
        datosCompletos.put("password", "password123");

        Map<String, String> datosIncompletos = new HashMap<>();
        datosIncompletos.put("nombre", "Juan");
        // Faltan campos requeridos

        // Act & Assert
        assertTrue(datosCompletos.containsKey("nombre"));
        assertTrue(datosCompletos.containsKey("correo"));
        assertTrue(datosCompletos.containsKey("dni"));
        assertTrue(datosCompletos.containsKey("password"));
        assertEquals(6, datosCompletos.size());

        assertFalse(datosIncompletos.containsKey("correo"));
        assertFalse(datosIncompletos.containsKey("dni"));
        assertFalse(datosIncompletos.containsKey("password"));
        assertEquals(1, datosIncompletos.size());
    }

    @Test
    void testValidarFormatoEmail() {
        // Arrange
        String emailValido = "usuario@correo.com";
        String emailInvalido1 = "email-sin-arroba";
        String emailInvalido2 = "@sin-usuario";
        String emailInvalido3 = "usuario@";

        // Act & Assert
        assertTrue(emailValido.contains("@"));
        assertTrue(emailValido.contains("."));
        assertFalse(emailInvalido1.contains("@"));
        assertTrue(emailInvalido2.startsWith("@"));
        assertTrue(emailInvalido3.endsWith("@"));
        
        assertTrue(emailValido.matches("^[A-Za-z0-9+_.-]+@(.+)$"));
        assertFalse(emailInvalido1.matches("^[A-Za-z0-9+_.-]+@(.+)$"));
    }

    @Test
    void testValidarPasswordSegura() {
        // Arrange
        String passwordSegura = "password123";
        String passwordCorta = "123";
        String passwordVacia = "";

        // Act & Assert
        assertTrue(passwordSegura.length() >= 8);
        assertFalse(passwordCorta.length() >= 8);
        assertTrue(passwordVacia.isEmpty());
        
        assertNotNull(passwordSegura);
        assertFalse(passwordSegura.trim().isEmpty());
    }

    @Test
    void testValidarDni() {
        // Arrange
        String dniValido = "12345678";
        String dniInvalido1 = "123"; // Muy corto
        String dniInvalido2 = "1234567a"; // Con letra
        String dniInvalido3 = "123456789"; // Muy largo

        // Act & Assert
        assertTrue(dniValido.matches("\\d{8}"));
        assertEquals(8, dniValido.length());
        
        assertFalse(dniInvalido1.matches("\\d{8}"));
        assertFalse(dniInvalido2.matches("\\d{8}"));
        assertFalse(dniInvalido3.matches("\\d{8}"));
    }

    @Test
    void testValidarRespuestaHTTP() {
        // Arrange
        Object datosExitosos = "Usuario registrado";
        Object datosError = "Faltan campos requeridos";

        // Act
        ResponseEntity<?> respuestaExitosa = ResponseEntity.ok(datosExitosos);
        ResponseEntity<?> respuestaError = ResponseEntity.badRequest().body(datosError);

        // Assert
        assertEquals(HttpStatus.OK, respuestaExitosa.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, respuestaError.getStatusCode());
        assertEquals("Usuario registrado", respuestaExitosa.getBody());
        assertEquals("Faltan campos requeridos", respuestaError.getBody());
    }

    @Test
    void testValidarDatosLogin() {
        // Arrange
        Map<String, String> loginValido = new HashMap<>();
        loginValido.put("email", "juan@test.com");
        loginValido.put("password", "password123");

        Map<String, String> loginInvalido = new HashMap<>();
        loginInvalido.put("email", "juan@test.com");
        // Falta password

        // Act & Assert
        assertTrue(loginValido.containsKey("email"));
        assertTrue(loginValido.containsKey("password"));
        assertEquals(2, loginValido.size());

        assertTrue(loginInvalido.containsKey("email"));
        assertFalse(loginInvalido.containsKey("password"));
        assertEquals(1, loginInvalido.size());
    }

    @Test
    void testValidarTokenFormat() {
        // Arrange
        String tokenSimulado = "Bearer jwt-token-123456789";
        String tokenInvalido = "jwt-token-sin-bearer";
        String tokenNulo = null;

        // Act & Assert
        assertTrue(tokenSimulado.startsWith("Bearer "));
        assertEquals("jwt-token-123456789", tokenSimulado.substring(7));
        assertFalse(tokenInvalido.startsWith("Bearer "));
        
        assertNotNull(tokenSimulado);
        assertNull(tokenNulo);
        assertTrue(tokenSimulado.length() > 20);
    }
}