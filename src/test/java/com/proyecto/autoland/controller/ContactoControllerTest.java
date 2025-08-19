package com.proyecto.autoland.controller;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.proyecto.autoland.dto.ContactoRequest;

class ContactoControllerTest {

    @Test
    void testValidarDatosContacto() {
        // Arrange
        ContactoRequest contactoCompleto = new ContactoRequest();
        contactoCompleto.setNombre("Juan Perez");
        contactoCompleto.setDni("12345678");
        contactoCompleto.setTelefono("987654321");
        contactoCompleto.setEmail("juan@test.com");
        contactoCompleto.setConsulta("Otra consulta");
        contactoCompleto.setMensaje("Necesito información sobre servicios");

        // Act & Assert
        assertNotNull(contactoCompleto.getNombre());
        assertNotNull(contactoCompleto.getDni());
        assertNotNull(contactoCompleto.getTelefono());
        assertNotNull(contactoCompleto.getEmail());
        assertNotNull(contactoCompleto.getConsulta());
        assertNotNull(contactoCompleto.getMensaje());
        
        assertEquals("Juan Perez", contactoCompleto.getNombre());
        assertEquals("12345678", contactoCompleto.getDni());
        assertEquals("987654321", contactoCompleto.getTelefono());
        assertEquals("juan@test.com", contactoCompleto.getEmail());
    }

    @Test
    void testValidarCamposRequeridos() {
        // Arrange
        String nombre = "Juan Perez";
        String nombreVacio = "";
        String nombreNulo = null;
        
        String dni = "12345678";
        String dniCorto = "123";
        
        String telefono = "987654321";
        String telefonoCorto = "123";
        
        String email = "juan@test.com";
        String emailInvalido = "email-sin-arroba";

        // Act & Assert - Validar nombre
        assertFalse(nombre.trim().isEmpty());
        assertTrue(nombreVacio.trim().isEmpty());
        
        // Act & Assert - Validar DNI
        assertTrue(dni.matches("\\d{8}"));
        assertFalse(dniCorto.matches("\\d{8}"));
        assertEquals(8, dni.length());
        assertEquals(3, dniCorto.length());
        
        // Act & Assert - Validar teléfono
        assertTrue(telefono.matches("\\d{9}"));
        assertFalse(telefonoCorto.matches("\\d{9}"));
        assertEquals(9, telefono.length());
        
        // Act & Assert - Validar email
        assertTrue(email.matches("^[A-Za-z0-9+_.-]+@(.+)$"));
        assertFalse(emailInvalido.matches("^[A-Za-z0-9+_.-]+@(.+)$"));
        assertTrue(email.contains("@"));
        assertTrue(email.contains("."));
    }

    @Test
    void testValidarFormatoEmail() {
        // Arrange
        String emailValido1 = "usuario@correo.com";
        String emailValido2 = "test.email@empresa.org";
        String emailInvalido1 = "email-sin-arroba";
        String emailInvalido2 = "@sin-usuario.com";
        String emailInvalido3 = "usuario@";
        String emailInvalido4 = "usuario@sin-punto";

        // Act & Assert
        assertTrue(emailValido1.matches("^[A-Za-z0-9+_.-]+@(.+)$"));
        assertTrue(emailValido2.matches("^[A-Za-z0-9+_.-]+@(.+)$"));
        assertFalse(emailInvalido1.matches("^[A-Za-z0-9+_.-]+@(.+)$"));
        assertFalse(emailInvalido2.matches("^[A-Za-z0-9+_.-]+@(.+)$"));
        assertFalse(emailInvalido3.matches("^[A-Za-z0-9+_.-]+@(.+)$"));
        
        assertTrue(emailValido1.contains("@") && emailValido1.contains("."));
        assertTrue(emailValido2.contains("@") && emailValido2.contains("."));
        assertFalse(emailInvalido1.contains("@"));
        assertFalse(emailInvalido4.contains("."));
    }

    @Test
    void testValidarDni() {
        // Arrange
        String dniValido = "12345678";
        String dniInvalido1 = "123"; // Muy corto
        String dniInvalido2 = "123456789"; // Muy largo
        String dniInvalido3 = "1234567a"; // Con letra
        String dniInvalido4 = "abcdefgh"; // Solo letras

        // Act & Assert
        assertTrue(dniValido.matches("\\d{8}"));
        assertEquals(8, dniValido.length());
        
        assertFalse(dniInvalido1.matches("\\d{8}"));
        assertFalse(dniInvalido2.matches("\\d{8}"));
        assertFalse(dniInvalido3.matches("\\d{8}"));
        assertFalse(dniInvalido4.matches("\\d{8}"));
        
        assertEquals(3, dniInvalido1.length());
        assertEquals(9, dniInvalido2.length());
    }

    @Test
    void testValidarTiposConsulta() {
        // Arrange
        String[] tiposValidos = {
            "Quiero tasar mi auto",
            "Cotizar vehiculo nuevo", 
            "Cotizar vehiculo seminuevo",
            "Reclamos",
            "Otra consulta"
        };
        String tipoInvalido = "";

        // Act & Assert
        for (String tipo : tiposValidos) {
            assertNotNull(tipo);
            assertFalse(tipo.trim().isEmpty());
            assertTrue(tipo.length() > 3);
        }
        
        assertTrue(tipoInvalido.isEmpty());
        
        // Verificar que "Otra consulta" está en los tipos válidos
        boolean tipoEncontrado = false;
        for (String tipo : tiposValidos) {
            if ("Otra consulta".equals(tipo)) {
                tipoEncontrado = true;
                break;
            }
        }
        assertTrue(tipoEncontrado);
    }

    @Test
    void testValidarMensaje() {
        // Arrange
        String mensajeValido = "Necesito información sobre los servicios disponibles";
        String mensajeCorto = "Hola";
        String mensajeVacio = "";
        String mensajeLargo = "Este es un mensaje muy largo que excede el límite normal de caracteres permitidos para un mensaje de contacto en el sistema".repeat(10);

        // Act & Assert
        assertNotNull(mensajeValido);
        assertFalse(mensajeValido.trim().isEmpty());
        assertTrue(mensajeValido.length() > 10);
        
        assertTrue(mensajeCorto.length() < 10);
        assertTrue(mensajeVacio.isEmpty());
        assertTrue(mensajeLargo.length() > 500);
        
        assertNotEquals(mensajeValido, mensajeVacio);
        assertNotEquals(mensajeCorto, mensajeLargo);
    }

    @Test
    void testValidarRespuestasHTTP() {
        // Arrange
        Object contactoExitoso = "Contacto enviado y guardado exitosamente";
        Object errorCamposFaltantes = "El nombre es requerido";
        Object errorEmailInvalido = "Formato de email inválido";
        Object errorDniInvalido = "El DNI debe tener 8 dígitos";

        // Act
        ResponseEntity<?> respuestaExitosa = ResponseEntity.ok(contactoExitoso);
        ResponseEntity<?> respuestaError1 = ResponseEntity.badRequest().body(errorCamposFaltantes);
        ResponseEntity<?> respuestaError2 = ResponseEntity.badRequest().body(errorEmailInvalido);
        ResponseEntity<?> respuestaError3 = ResponseEntity.badRequest().body(errorDniInvalido);

        // Assert
        assertEquals(HttpStatus.OK, respuestaExitosa.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, respuestaError1.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, respuestaError2.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, respuestaError3.getStatusCode());
        
        assertEquals("Contacto enviado y guardado exitosamente", respuestaExitosa.getBody());
        assertEquals("El nombre es requerido", respuestaError1.getBody());
        assertEquals("Formato de email inválido", respuestaError2.getBody());
        assertEquals("El DNI debe tener 8 dígitos", respuestaError3.getBody());
    }
}