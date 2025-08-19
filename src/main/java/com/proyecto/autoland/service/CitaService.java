package com.proyecto.autoland.service;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.autoland.dto.CitaDTO;
import com.proyecto.autoland.dto.CrearCitaRequest;
import com.proyecto.autoland.model.Cita;
import com.proyecto.autoland.model.Usuario;
import com.proyecto.autoland.repository.CitaRepository;
import com.proyecto.autoland.repository.UsuarioRepository;

@Service
public class CitaService {

    @Autowired
    private CitaRepository citaRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    private String convertirFecha(String fecha) {
        if (fecha == null || fecha.isEmpty()) {
            return null;
        }
        
        try {
            if (fecha.matches("\\d{4}-\\d{2}-\\d{2}")) {
                LocalDate date = LocalDate.parse(fecha, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                return date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            }
            else if (fecha.matches("\\d{2}-\\d{2}-\\d{4}")) {
                return fecha;
            }
            else {
                System.out.println("⚠️ Formato de fecha no reconocido: " + fecha);
                return fecha;
            }
        } catch (DateTimeParseException e) {
            System.out.println("⚠️ Error al parsear fecha: " + fecha);
            return fecha;
        }
    }

    private CitaDTO toDTO(Cita cita) {
        CitaDTO dto = new CitaDTO();
        dto.setId_cita(cita.getId_cita());
        dto.setPlaca(cita.getPlaca());
        dto.setMarca(cita.getMarca());
        dto.setModelo(cita.getModelo());
        dto.setAnio(cita.getAnio());
        dto.setServicio(cita.getServicio());
        dto.setSucursal(cita.getSucursal());
        dto.setFecha(cita.getFecha());
        dto.setHorario(cita.getHorario());
        dto.setEstado(cita.getEstado());
        dto.setFecha_creacion(cita.getFecha_creacion());
        dto.setObservaciones(cita.getObservaciones());
        
        if (cita.getUsuario() != null) {
            dto.setUsuarioId(cita.getUsuario().getIdUsuario());
            dto.setUsuario_nombre(cita.getUsuario().getNombre());
            dto.setUsuario_email(cita.getUsuario().getEmail());
        }
        
        return dto;
    }

    public CitaDTO crearCita(CrearCitaRequest request, Integer usuarioId) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }
        
        Long citasActivas = citaRepository.contarCitasActivasPorUsuario(usuarioId);
        if (citasActivas >= 4) {
            throw new RuntimeException("Has alcanzado el límite máximo de 4 citas. Cancela alguna cita existente para agendar una nueva.");
        }
        
        Long citasEnHorario = citaRepository.contarCitasEnHorario(
            request.getFecha(), 
            request.getHorario(), 
            request.getSucursal()
        );
        
        if (citasEnHorario >= 3) {
            throw new RuntimeException("Horario no disponible. Selecciona otro horario.");
        }
        
        Cita cita = new Cita();
        cita.setPlaca(request.getPlaca());
        cita.setMarca(request.getMarca());
        cita.setModelo(request.getModelo());
        cita.setAnio(request.getAnio());
        cita.setServicio(request.getServicio());
        cita.setSucursal(request.getSucursal());
        cita.setFecha(request.getFecha());
        cita.setHorario(request.getHorario());
        cita.setObservaciones(request.getObservaciones());
        cita.setUsuario(usuarioOpt.get());
        
        Cita citaGuardada = citaRepository.save(cita);
        return toDTO(citaGuardada);
    }
    
    public CitaDTO buscarCitaPorDatos(String placa, String dni, String fecha, String sucursal) {
        System.out.println("=== DEBUG BUSCAR CITA ===");
        System.out.println("Buscando con - Placa: '" + placa + "', DNI: '" + dni + "', Fecha: '" + fecha + "', Sucursal: '" + sucursal + "'");
        
        String fechaNormalizada = convertirFecha(fecha);
        System.out.println("📅 Fecha normalizada: '" + fecha + "' -> '" + fechaNormalizada + "'");
        
        Optional<Usuario> usuarioOpt = usuarioRepository.findByDni(dni);
        if (usuarioOpt.isEmpty()) {
            System.out.println("ERROR: No se encontró usuario con DNI: " + dni);
            throw new RuntimeException("No se encontró usuario con el DNI proporcionado");
        }
        
        Usuario usuario = usuarioOpt.get();
        System.out.println("✅ Usuario encontrado: " + usuario.getNombre() + " (ID: " + usuario.getIdUsuario() + ")");
        
        List<Cita> citas = citaRepository.findByUsuarioIdAndEstadoNotCancelada(usuario.getIdUsuario());
        System.out.println("📋 Total citas activas del usuario: " + citas.size());
        
        for (int i = 0; i < citas.size(); i++) {
            Cita cita = citas.get(i);
            System.out.println("Cita " + (i+1) + ": ID=" + cita.getId_cita() + 
                              ", Placa='" + cita.getPlaca() + "'" +
                              ", Fecha='" + cita.getFecha() + "'" +
                              ", Sucursal='" + cita.getSucursal() + "'" +
                              ", Estado='" + cita.getEstado() + "'");
        }
        
        System.out.println("🔍 Aplicando filtros...");
        
        Optional<Cita> citaEncontrada = citas.stream()
            .filter(cita -> {
                boolean coincidePlaca = cita.getPlaca().equalsIgnoreCase(placa);
                System.out.println("   Filtro placa: '" + cita.getPlaca() + "' vs '" + placa + "' = " + coincidePlaca);
                return coincidePlaca;
            })
            .filter(cita -> {
                boolean coincideFecha = fechaNormalizada == null || cita.getFecha().equals(fechaNormalizada);
                System.out.println("   Filtro fecha: '" + cita.getFecha() + "' vs '" + fechaNormalizada + "' = " + coincideFecha);
                return coincideFecha;
            })
            .filter(cita -> {
                boolean coincideSucursal = sucursal == null || cita.getSucursal().equalsIgnoreCase(sucursal);
                System.out.println("   Filtro sucursal: '" + cita.getSucursal() + "' vs '" + sucursal + "' = " + coincideSucursal);
                return coincideSucursal;
            })
            .findFirst();
            
        if (citaEncontrada.isEmpty()) {
            System.out.println("❌ No se encontró ninguna cita después de aplicar filtros");
            throw new RuntimeException("No se encontró ninguna cita con los datos proporcionados");
        }
        
        System.out.println("✅ Cita encontrada: ID=" + citaEncontrada.get().getId_cita());
        return toDTO(citaEncontrada.get());
    }
    
    public CitaDTO actualizarCita(Integer citaId, CrearCitaRequest request, Integer usuarioId) {
        Optional<Cita> citaOpt = citaRepository.findById(citaId);
        if (citaOpt.isEmpty()) {
            throw new RuntimeException("Cita no encontrada");
        }
        
        Cita cita = citaOpt.get();
        
        if (!cita.getUsuario().getIdUsuario().equals(usuarioId)) {
            throw new RuntimeException("No tienes permisos para modificar esta cita");
        }
        
        if (!cita.getFecha().equals(request.getFecha()) || 
            !cita.getHorario().equals(request.getHorario()) || 
            !cita.getSucursal().equals(request.getSucursal())) {
            
            Long citasEnHorario = citaRepository.contarCitasEnHorario(
                request.getFecha(), 
                request.getHorario(), 
                request.getSucursal()
            );
            
            if (citasEnHorario >= 3) {
                throw new RuntimeException("El nuevo horario no está disponible. Selecciona otro horario.");
            }
        }
        
        cita.setPlaca(request.getPlaca());
        cita.setMarca(request.getMarca());
        cita.setModelo(request.getModelo());
        cita.setAnio(request.getAnio());
        cita.setServicio(request.getServicio());
        cita.setSucursal(request.getSucursal());
        cita.setFecha(request.getFecha());
        cita.setHorario(request.getHorario());
        cita.setObservaciones(request.getObservaciones());
        
        Cita citaActualizada = citaRepository.save(cita);
        return toDTO(citaActualizada);
    }

    public List<CitaDTO> obtenerCitasPorUsuario(Integer usuarioId) {
        List<Cita> citas = citaRepository.findByUsuarioIdAndEstadoNotCancelada(usuarioId);
        return citas.stream().map(this::toDTO).toList();
    }

    public Optional<CitaDTO> obtenerCitaPorId(Integer citaId) {
        return citaRepository.findById(citaId)
                .map(this::toDTO);
    }

    public CitaDTO actualizarEstadoCita(Integer citaId, String nuevoEstado) {
        Optional<Cita> citaOpt = citaRepository.findById(citaId);
        if (citaOpt.isEmpty()) {
            throw new RuntimeException("Cita no encontrada");
        }
        
        Cita cita = citaOpt.get();
        cita.setEstado(nuevoEstado);
        
        Cita citaActualizada = citaRepository.save(cita);
        return toDTO(citaActualizada);
    }

    public void cancelarCita(Integer citaId, Integer usuarioId) {
        Optional<Cita> citaOpt = citaRepository.findById(citaId);
        if (citaOpt.isEmpty()) {
            throw new RuntimeException("Cita no encontrada");
        }
        
        Cita cita = citaOpt.get();
        
        if (!cita.getUsuario().getIdUsuario().equals(usuarioId)) {
            throw new RuntimeException("No tienes permisos para cancelar esta cita");
        }
        
        cita.setEstado("cancelada");
        citaRepository.save(cita);
    }

    public List<CitaDTO> obtenerTodasLasCitas() {
        List<Cita> citas = citaRepository.findAll();
        return citas.stream().map(this::toDTO).toList();
    }
}