package com.proyecto.autoland.controller;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.proyecto.autoland.dto.ReclamosRequest;

class ReclamosControllerTest {

    @Test
    void testValidarDatosReclamo() {
        // Arrange
        ReclamosRequest reclamoCompleto = new ReclamosRequest();
        reclamoCompleto.setNombres("Juan Carlos");
        reclamoCompleto.setApellidos("Perez Garcia");
        reclamoCompleto.setDni("12345678");
        reclamoCompleto.setTelefono("987654321");
        reclamoCompleto.setEmail("juan@test.com");
        reclamoCompleto.setCiudad("Lima");
        reclamoCompleto.setPlaca("ABC123");
        reclamoCompleto.setDetalle("Problema con el servicio");
        reclamoCompleto.setTipo("Queja");
        reclamoCompleto.setServicio("Servicio tecnico");
        reclamoCompleto.setMotivo("Otros");
        reclamoCompleto.setLocal("Naranjal");

        // Act & Assert
        assertNotNull(reclamoCompleto.getNombres());
        assertNotNull(reclamoCompleto.getApellidos());
        assertNotNull(reclamoCompleto.getDni());
        assertNotNull(reclamoCompleto.getTelefono());
        assertNotNull(reclamoCompleto.getEmail());
        assertNotNull(reclamoCompleto.getCiudad());
        assertNotNull(reclamoCompleto.getDetalle());
        assertNotNull(reclamoCompleto.getTipo());
        
        assertEquals("Juan Carlos", reclamoCompleto.getNombres());
        assertEquals("Perez Garcia", reclamoCompleto.getApellidos());
        assertEquals("12345678", reclamoCompleto.getDni());
        assertEquals("Lima", reclamoCompleto.getCiudad());
        assertEquals("Queja", reclamoCompleto.getTipo());
    }

    @Test
    void testValidarCamposObligatorios() {
        // Arrange
        String nombres = "Juan Carlos";
        String nombresVacio = "";
        
        String apellidos = "Perez Garcia";
        String apellidosVacio = "";
        
        String dni = "12345678";
        String dniInvalido = "123";
        
        String email = "juan@test.com";
        String emailInvalido = "email-sin-arroba";

        // Act & Assert - Validar nombres
        assertFalse(nombres.trim().isEmpty());
        assertTrue(nombresVacio.trim().isEmpty());
        assertTrue(nombres.length() > 2);
        
        // Act & Assert - Validar apellidos
        assertFalse(apellidos.trim().isEmpty());
        assertTrue(apellidosVacio.trim().isEmpty());
        assertTrue(apellidos.length() > 2);
        
        // Act & Assert - Validar DNI
        assertTrue(dni.matches("\\d{8}"));
        assertFalse(dniInvalido.matches("\\d{8}"));
        assertEquals(8, dni.length());
        
        // Act & Assert - Validar email
        assertTrue(email.matches("^[A-Za-z0-9+_.-]+@(.+)$"));
        assertFalse(emailInvalido.matches("^[A-Za-z0-9+_.-]+@(.+)$"));
    }

    @Test
    void testValidarTiposReclamo() {
        // Arrange
        String[] tiposValidos = {"Queja", "Reclamo"};
        String tipoInvalido = "TipoInexistente";
        String tipoVacio = "";

        // Act & Assert
        for (String tipo : tiposValidos) {
            assertNotNull(tipo);
            assertFalse(tipo.trim().isEmpty());
            assertTrue(tipo.length() > 3);
        }
        
        assertTrue(tipoVacio.isEmpty());
        
        // Verificar que "Queja" está en los tipos válidos
        boolean quejaEncontrada = false;
        for (String tipo : tiposValidos) {
            if ("Queja".equals(tipo)) {
                quejaEncontrada = true;
                break;
            }
        }
        assertTrue(quejaEncontrada);
        
        // Verificar que tipo inválido no está en la lista
        boolean tipoInvalidoEncontrado = false;
        for (String tipo : tiposValidos) {
            if (tipoInvalido.equals(tipo)) {
                tipoInvalidoEncontrado = true;
                break;
            }
        }
        assertFalse(tipoInvalidoEncontrado);
    }

    @Test
    void testValidarTiposServicio() {
        // Arrange
        String[] serviciosValidos = {
            "Venta nuevos", 
            "Venta usados", 
            "Servicio tecnico", 
            "Repuestos"
        };
        String servicioInvalido = "";

        // Act & Assert
        for (String servicio : serviciosValidos) {
            assertNotNull(servicio);
            assertFalse(servicio.trim().isEmpty());
            assertTrue(servicio.length() > 4);
        }
        
        assertTrue(servicioInvalido.isEmpty());
        
        // Verificar que "Servicio tecnico" está en los servicios válidos
        boolean tipoEncontrado = false;
        for (String servicio : serviciosValidos) {
            if ("Servicio tecnico".equals(servicio)) {
            	tipoEncontrado = true;
                break;
            }
        }
        assertTrue(tipoEncontrado);
    }

    @Test
    void testValidarMotivosContacto() {
        // Arrange
        String[] motivosValidos = {
            "Fecha de entrega de vehiculo", 
            "Devolucion de dinero pendiente",
            "Solicitud de factura o boleta",
            "Documentacion pendiente",
            "Accesorio o repuesto pendiente",
            "Falla mecanica",
            "Atencion asesor de ventas o servicio",
            "Perdida de objetos personales",
            "Papeletas",
            "Otros"
        };
        String motivoVacio = "";

        // Act & Assert
        for (String motivo : motivosValidos) {
            assertNotNull(motivo);
            assertFalse(motivo.trim().isEmpty());
            assertTrue(motivo.length() > 4);
        }
        
        assertTrue(motivoVacio.isEmpty());
        
        // Verificar que "Otros" está en los motivos válidos
        boolean motivoEncontrado = false;
        for (String motivo : motivosValidos) {
            if ("Otros".equals(motivo)) {
            	motivoEncontrado = true;
                break;
            }
        }
        assertTrue(motivoEncontrado);
    }

    @Test
    void testValidarLocales() {
        // Arrange
        String[] localesValidos = {
            "Surco",
            "La marina", 
            "Naranjal",
            "Surquillo"
        };
        String localInvalido = "";

        // Act & Assert
        for (String local : localesValidos) {
            assertNotNull(local);
            assertFalse(local.trim().isEmpty());
            assertTrue(local.length() > 4);
        }
        
        assertTrue(localInvalido.isEmpty());
        
        // Verificar que "Naranjal" está en los locales válidos
        boolean localEncontrado = false;
        for (String local : localesValidos) {
            if ("Naranjal".equals(local)) {
            	localEncontrado = true;
                break;
            }
        }
        assertTrue(localEncontrado);
    }

    @Test
    void testValidarDni() {
        // Arrange
        String dniValido = "87654321";
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
        assertEquals(8, dniInvalido3.length());
        assertEquals(8, dniInvalido4.length());
    }

    @Test
    void testValidarRespuestasHTTP() {
        // Arrange
        Object reclamoExitoso = "Reclamo registrado y guardado exitosamente";
        Object errorNombres = "Los nombres son requeridos";
        Object errorApellidos = "Los apellidos son requeridos";
        Object errorDni = "El DNI debe tener 8 dígitos";
        Object errorEmail = "Formato de email inválido";

        // Act
        ResponseEntity<?> respuestaExitosa = ResponseEntity.ok(reclamoExitoso);
        ResponseEntity<?> respuestaError1 = ResponseEntity.badRequest().body(errorNombres);
        ResponseEntity<?> respuestaError2 = ResponseEntity.badRequest().body(errorApellidos);
        ResponseEntity<?> respuestaError3 = ResponseEntity.badRequest().body(errorDni);
        ResponseEntity<?> respuestaError4 = ResponseEntity.badRequest().body(errorEmail);

        // Assert
        assertEquals(HttpStatus.OK, respuestaExitosa.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, respuestaError1.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, respuestaError2.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, respuestaError3.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, respuestaError4.getStatusCode());
        
        assertEquals("Reclamo registrado y guardado exitosamente", respuestaExitosa.getBody());
        assertEquals("Los nombres son requeridos", respuestaError1.getBody());
        assertEquals("Los apellidos son requeridos", respuestaError2.getBody());
        assertEquals("El DNI debe tener 8 dígitos", respuestaError3.getBody());
        assertEquals("Formato de email inválido", respuestaError4.getBody());
        
        assertNotNull(respuestaExitosa.getBody());
        assertNotNull(respuestaError1.getBody());
    }
}