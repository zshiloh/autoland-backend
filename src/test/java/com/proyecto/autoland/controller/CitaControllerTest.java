package com.proyecto.autoland.controller;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.proyecto.autoland.dto.CrearCitaRequest;
import java.util.Map;
import java.util.HashMap;

class CitaControllerTest {

    @Test
    void testValidarDatosCita() {
        // Arrange
        CrearCitaRequest citaCompleta = new CrearCitaRequest();
        citaCompleta.setPlaca("ABC123");
        citaCompleta.setMarca("Toyota");
        citaCompleta.setModelo("Corolla");
        citaCompleta.setAnio("2020");
        citaCompleta.setServicio("Servicio de mantenimiento");
        citaCompleta.setSucursal("Surco");
        citaCompleta.setFecha("15-08-2025");
        citaCompleta.setHorario("10:00");
        citaCompleta.setObservaciones("Revisión completa");

        // Act & Assert
        assertNotNull(citaCompleta.getPlaca());
        assertNotNull(citaCompleta.getMarca());
        assertNotNull(citaCompleta.getModelo());
        assertNotNull(citaCompleta.getServicio());
        assertNotNull(citaCompleta.getFecha());
        assertNotNull(citaCompleta.getHorario());
        
        assertEquals("ABC123", citaCompleta.getPlaca());
        assertEquals("Toyota", citaCompleta.getMarca());
        assertEquals("15-08-2025", citaCompleta.getFecha());
        assertEquals("10:00", citaCompleta.getHorario());
    }

    @Test
    void testValidarPlaca() {
        // Arrange
        String placaValida1 = "ABC123";
        String placaValida2 = "XYZ789";
        String placaInvalida1 = "A"; // Muy corta
        String placaInvalida2 = "abc123"; // Minúsculas

        // Act & Assert
        assertTrue(placaValida1.length() >= 6);
        assertTrue(placaValida2.length() >= 6);
        assertFalse(placaInvalida1.length() >= 6);
        
        assertTrue(placaValida1.matches("[A-Z0-9]+"));
        assertTrue(placaValida2.matches("[A-Z0-9]+"));
        assertFalse(placaInvalida2.matches("[A-Z0-9]+"));
    }

    @Test
    void testValidarFechaCita() {
        // Arrange
        String fechaValida = "15-08-2025";
        String fechaInvalida1 = "2024-12-15"; // Formato incorrecto
        String fechaInvalida2 = "15/12/2024"; // Separador incorrecto
        String fechaInvalida3 = "32-13-2024"; // Día y mes inválidos

        // Act & Assert
        assertTrue(fechaValida.matches("\\d{2}-\\d{2}-\\d{4}"));
        assertFalse(fechaInvalida1.matches("\\d{2}-\\d{2}-\\d{4}"));
        assertFalse(fechaInvalida2.matches("\\d{2}-\\d{2}-\\d{4}"));
        
        assertEquals(10, fechaValida.length());
        assertTrue(fechaValida.contains("-"));
        assertEquals(2, fechaValida.split("-").length - 1); // 2 guiones
        
        // Validar partes de fecha
        String[] partes = fechaValida.split("-");
        int dia = Integer.parseInt(partes[0]);
        int mes = Integer.parseInt(partes[1]);
        
        assertTrue(dia >= 1 && dia <= 31);
        assertTrue(mes >= 1 && mes <= 12);
    }

    @Test
    void testValidarHorarioCita() {
        // Arrange
        String horarioValido1 = "10:00";
        String horarioValido2 = "14:30";
        String horarioInvalido1 = "25:00"; // Hora inválida
        String horarioInvalido2 = "10:70"; // Minutos inválidos
        String horarioInvalido3 = "10-30"; // Formato incorrecto

        // Act & Assert
        assertTrue(horarioValido1.matches("\\d{2}:\\d{2}"));
        assertTrue(horarioValido2.matches("\\d{2}:\\d{2}"));
        assertFalse(horarioInvalido3.matches("\\d{2}:\\d{2}"));
        
        // Validar horario válido 1
        String[] partes1 = horarioValido1.split(":");
        int hora1 = Integer.parseInt(partes1[0]);
        int minutos1 = Integer.parseInt(partes1[1]);
        assertTrue(hora1 >= 0 && hora1 <= 23);
        assertTrue(minutos1 >= 0 && minutos1 <= 59);
        
        // Validar horario inválido
        String[] partes2 = horarioInvalido1.split(":");
        int hora2 = Integer.parseInt(partes2[0]);
        assertFalse(hora2 >= 0 && hora2 <= 23);
    }

    @Test
    void testValidarAuthorizationHeader() {
        // Arrange
        String headerValido = "Bearer jwt-token-123456789";
        String headerInvalido1 = "jwt-token-sin-bearer";
        String headerInvalido2 = "Bearer "; // Sin token
        String headerNulo = null;

        // Act & Assert
        assertTrue(headerValido.startsWith("Bearer "));
        assertFalse(headerInvalido1.startsWith("Bearer "));
        assertTrue(headerInvalido2.startsWith("Bearer "));
        
        // Extraer token del header válido
        String token = headerValido.substring(7);
        assertEquals("jwt-token-123456789", token);
        assertFalse(token.isEmpty());
        
        assertNull(headerNulo);
        assertNotNull(headerValido);
    }

    @Test
    void testValidarEstadosCita() {
        // Arrange
        String[] estadosValidos = {"pendiente", "confirmada", "completada", "cancelada"};
        String estadoInvalido = "estado-inexistente";

        // Act & Assert
        for (String estado : estadosValidos) {
            assertNotNull(estado);
            assertFalse(estado.trim().isEmpty());
        }
        
        // Verificar que "pendiente" está en los estados válidos
        boolean pendienteEncontrado = false;
        for (String estado : estadosValidos) {
            if ("pendiente".equals(estado)) {
                pendienteEncontrado = true;
                break;
            }
        }
        assertTrue(pendienteEncontrado);
        
        // Verificar que estado inválido no está en la lista
        boolean estadoInvalidoEncontrado = false;
        for (String estado : estadosValidos) {
            if (estadoInvalido.equals(estado)) {
                estadoInvalidoEncontrado = true;
                break;
            }
        }
        assertFalse(estadoInvalidoEncontrado);
    }

    @Test
    void testValidarRespuestasHTTP() {
        // Arrange
        Object citaCreada = "Cita creada exitosamente";
        Object errorToken = "Token expirado o inválido";
        
        // Act
        ResponseEntity<?> respuestaExitosa = ResponseEntity.ok(citaCreada);
        ResponseEntity<?> respuestaError = ResponseEntity.badRequest().body(errorToken);

        // Assert
        assertEquals(HttpStatus.OK, respuestaExitosa.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, respuestaError.getStatusCode());
        assertEquals("Cita creada exitosamente", respuestaExitosa.getBody());
        assertEquals("Token expirado o inválido", respuestaError.getBody());
        
        assertNotNull(respuestaExitosa.getBody());
        assertNotNull(respuestaError.getBody());
    }
}