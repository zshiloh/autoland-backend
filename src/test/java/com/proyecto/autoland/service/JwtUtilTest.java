package com.proyecto.autoland.service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.proyecto.autoland.security.JwtUtil;
import com.proyecto.autoland.model.Usuario;

class JwtUtilTest {

    private JwtUtil jwtUtil = new JwtUtil();

    @Test
    void testGenerarToken() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setEmail("test@test.com");
        usuario.setRol("cliente");

        // Act
        String token = jwtUtil.generarToken(usuario);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.length() > 50);
        assertTrue(token.contains("."));
        
        // JWT debe tener 3 partes separadas por punto
        String[] partes = token.split("\\.");
        assertEquals(3, partes.length);
    }

    @Test
    void testValidarTokenValido() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setEmail("test@test.com");
        usuario.setRol("cliente");
        String token = jwtUtil.generarToken(usuario);

        // Act
        boolean esValido = jwtUtil.validarToken(token);

        // Assert
        assertTrue(esValido);
    }

    @Test
    void testValidarTokenInvalido() {
        // Arrange
        String tokenInvalido = "token.invalido.malformado";

        // Act
        boolean esValido = jwtUtil.validarToken(tokenInvalido);

        // Assert
        assertFalse(esValido);
    }

    @Test
    void testExtraerEmailDelToken() {
        // Arrange
        String emailEsperado = "usuario@test.com";
        Usuario usuario = new Usuario();
        usuario.setEmail(emailEsperado);
        usuario.setRol("cliente");
        String token = jwtUtil.generarToken(usuario);

        // Act
        String emailExtraido = jwtUtil.extraerEmail(token);

        // Assert
        assertEquals(emailEsperado, emailExtraido);
        assertNotNull(emailExtraido);
        assertTrue(emailExtraido.contains("@"));
    }

    @Test
    void testTokensUnicos() {
        // Arrange
        Usuario usuario1 = new Usuario();
        usuario1.setEmail("usuario1@test.com");
        usuario1.setRol("cliente");
        
        Usuario usuario2 = new Usuario();
        usuario2.setEmail("usuario2@test.com");
        usuario2.setRol("admin");

        // Act
        String token1 = jwtUtil.generarToken(usuario1);
        String token2 = jwtUtil.generarToken(usuario2);

        // Assert
        assertNotEquals(token1, token2);
        assertNotNull(token1);
        assertNotNull(token2);
        
        assertEquals("usuario1@test.com", jwtUtil.extraerEmail(token1));
        assertEquals("usuario2@test.com", jwtUtil.extraerEmail(token2));
    }

    @Test
    void testValidarTokenNulo() {
        // Arrange
        String tokenNulo = null;

        // Act
        boolean esValido = jwtUtil.validarToken(tokenNulo);

        // Assert
        assertFalse(esValido);
    }

    @Test
    void testValidarTokenVacio() {
        // Arrange
        String tokenVacio = "";

        // Act
        boolean esValido = jwtUtil.validarToken(tokenVacio);

        // Assert
        assertFalse(esValido);
    }
}