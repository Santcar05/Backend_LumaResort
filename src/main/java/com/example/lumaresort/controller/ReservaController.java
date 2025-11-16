package com.example.lumaresort.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.lumaresort.dto.ActualizarReservaRequest;
import com.example.lumaresort.entities.Habitacion;
import com.example.lumaresort.entities.Reserva;
import com.example.lumaresort.entities.Servicio;
import com.example.lumaresort.entities.Usuario;
import com.example.lumaresort.repository.HabitacionRepository;
import com.example.lumaresort.repository.ReservaRepository;
import com.example.lumaresort.repository.ServicioRepository;
import com.example.lumaresort.repository.UsuarioRepository;
import com.example.lumaresort.service.ReservaService;

@RestController
@RequestMapping("/reservas")
@CrossOrigin(origins = "*")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;
    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private HabitacionRepository habitacionRepository;
    @Autowired
    private ServicioRepository servicioRepository;

    @GetMapping
    public List<Reserva> getAll() {
        return reservaService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id, Authentication authentication) {
        Reserva reserva = reservaService.findById(id);

        if (reserva == null) {
            return ResponseEntity.notFound().build();
        }

        // Verificar que el usuario tenga acceso a esta reserva
        if (!tieneAccesoAReserva(authentication, reserva)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("mensaje", "No tiene permiso para ver esta reserva"));
        }

        return ResponseEntity.ok(reserva);
    }

    @PostMapping
    public Reserva create(@RequestBody Reserva reserva) {
        // Obtener las referencias persistentes (evita los null en las FK)
        if (reserva.getUsuario() != null && reserva.getUsuario().getIdUsuario() != null) {
            Usuario usuario = usuarioRepository.findById(reserva.getUsuario().getIdUsuario()).orElse(null);
            reserva.setUsuario(usuario);
        }

        if (reserva.getHabitacion() != null && reserva.getHabitacion().getIdHabitacion() != null) {
            Habitacion habitacion = habitacionRepository.findById(reserva.getHabitacion().getIdHabitacion()).orElse(null);
            reserva.setHabitacion(habitacion);
        }

        // Si hay servicios
        if (reserva.getServicios() != null && !reserva.getServicios().isEmpty()) {
            List<Servicio> serviciosPersistidos = new ArrayList<>();
            for (Servicio s : reserva.getServicios()) {
                servicioRepository.findById(s.getIdServicio()).ifPresent(serviciosPersistidos::add);
            }
            reserva.setServicios(serviciosPersistidos);
        }

        return reservaService.save(reserva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Reserva reserva, Authentication authentication) {
        Optional<Reserva> reservaExistenteOpt = reservaRepository.findById(id);

        if (reservaExistenteOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Reserva reservaExistente = reservaExistenteOpt.get();

        // Verificar que el usuario tenga acceso a modificar esta reserva
        if (!tieneAccesoAReserva(authentication, reservaExistente)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("mensaje", "No tiene permiso para modificar esta reserva"));
        }

        // Actualizar campos básicos
        if (reserva.getFechaInicio() != null) {
            reservaExistente.setFechaInicio(reserva.getFechaInicio());
        }
        if (reserva.getFechaFin() != null) {
            reservaExistente.setFechaFin(reserva.getFechaFin());
        }
        if (reserva.getCantidadPersonas() != null) {
            reservaExistente.setCantidadPersonas(reserva.getCantidadPersonas());
        }
        if (reserva.getEstado() != null) {
            reservaExistente.setEstado(reserva.getEstado());
        }

        // Manejar referencias persistentes
        if (reserva.getUsuario() != null && reserva.getUsuario().getIdUsuario() != null) {
            Usuario usuario = usuarioRepository.findById(reserva.getUsuario().getIdUsuario()).orElse(null);
            reservaExistente.setUsuario(usuario);
        }

        if (reserva.getHabitacion() != null && reserva.getHabitacion().getIdHabitacion() != null) {
            Habitacion habitacion = habitacionRepository.findById(reserva.getHabitacion().getIdHabitacion()).orElse(null);
            reservaExistente.setHabitacion(habitacion);
        }

        // Manejar servicios
        if (reserva.getServicios() != null) {
            List<Servicio> serviciosPersistidos = new ArrayList<>();
            for (Servicio s : reserva.getServicios()) {
                servicioRepository.findById(s.getIdServicio()).ifPresent(serviciosPersistidos::add);
            }
            reservaExistente.setServicios(serviciosPersistidos);
        }

        Reserva reservaActualizada = reservaRepository.save(reservaExistente);
        return ResponseEntity.ok(reservaActualizada);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarReserva(@PathVariable Long id, @RequestBody ActualizarReservaRequest request, Authentication authentication) {
        try {
            System.out.println("=== INICIANDO ACTUALIZACIÓN DE RESERVA ===");
            System.out.println("ID de reserva: " + id);
            System.out.println("Fecha inicio recibida: " + request.getFechaInicio());
            System.out.println("Fecha fin recibida: " + request.getFechaFin());
            System.out.println("Cantidad personas: " + request.getCantidadPersonas());
            System.out.println("ID Usuario: " + request.getIdUsuario());
            System.out.println("ID Habitacion: " + request.getIdHabitacion());

            Optional<Reserva> reservaOpt = reservaRepository.findById(id);

            if (reservaOpt.isEmpty()) {
                System.out.println("Reserva no encontrada con ID: " + id);
                return ResponseEntity.notFound().build();
            }

            Reserva reservaExistente = reservaOpt.get();
            System.out.println("Reserva existente - ID: " + reservaExistente.getIdReserva()
                    + ", Habitacion: " + (reservaExistente.getHabitacion() != null ? reservaExistente.getHabitacion().getIdHabitacion() : "null"));

            // Verificar que el usuario tenga acceso a modificar esta reserva
            if (!tieneAccesoAReserva(authentication, reservaExistente)) {
                System.out.println("Usuario sin permiso para modificar esta reserva");
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("message", "No tiene permiso para modificar esta reserva");
                errorResponse.put("success", false);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }

            // Validar que la reserva no esté cancelada
            if ("Cancelada".equalsIgnoreCase(reservaExistente.getEstado())) {
                System.out.println("Intento de actualizar reserva cancelada");
                return ResponseEntity.badRequest().body("No se puede actualizar una reserva cancelada");
            }

            // Validar que las nuevas fechas sean válidas
            if (request.getFechaInicio() != null && request.getFechaFin() != null) {
                System.out.println("Validando fechas...");
                System.out.println("Fecha inicio: " + request.getFechaInicio());
                System.out.println("Fecha fin: " + request.getFechaFin());

                if (request.getFechaFin().before(request.getFechaInicio())) {
                    System.out.println("Error: fecha fin anterior a fecha inicio");
                    return ResponseEntity.badRequest().body("La fecha de fin no puede ser anterior a la fecha de inicio");
                }

                // Validar que no se solape con otras reservas de la misma habitación
                List<Reserva> reservasMismaHabitacion = reservaRepository.findByHabitacionIdHabitacion(reservaExistente.getHabitacion().getIdHabitacion());
                System.out.println("Reservas misma habitación: " + reservasMismaHabitacion.size());

                List<Reserva> reservasSolapadas = new ArrayList<>();

                for (Reserva r : reservasMismaHabitacion) {
                    // Excluir la reserva actual y las canceladas
                    if (!r.getIdReserva().equals(id) && !"Cancelada".equalsIgnoreCase(r.getEstado())) {
                        System.out.println("Comparando con reserva: " + r.getIdReserva());
                        // Verificar solapamiento
                        if (request.getFechaInicio().before(r.getFechaFin())
                                && request.getFechaFin().after(r.getFechaInicio())) {
                            reservasSolapadas.add(r);
                            System.out.println("¡SOLAPAMIENTO DETECTADO con reserva: " + r.getIdReserva());
                        }
                    }
                }

                if (!reservasSolapadas.isEmpty()) {
                    System.out.println("Error: solapamiento con " + reservasSolapadas.size() + " reservas");
                    return ResponseEntity.badRequest().body("Las nuevas fechas se solapan con otra reserva existente");
                }
            }

            // Actualizar solo las fechas
            if (request.getFechaInicio() != null) {
                reservaExistente.setFechaInicio(request.getFechaInicio());
            }
            if (request.getFechaFin() != null) {
                reservaExistente.setFechaFin(request.getFechaFin());
            }

            // Actualizar cantidad de personas si se envía
            if (request.getCantidadPersonas() != null) {
                reservaExistente.setCantidadPersonas(request.getCantidadPersonas());
            }

            System.out.println("Guardando reserva actualizada...");
            Reserva reservaGuardada = reservaRepository.save(reservaExistente);
            System.out.println("Reserva guardada exitosamente - ID: " + reservaGuardada.getIdReserva());

            // CORRECCIÓN: Devolver un objeto JSON en lugar de texto plano
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Reserva actualizada correctamente");
            response.put("reserva", reservaGuardada);
            response.put("success", true);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("ERROR EN ACTUALIZACIÓN: " + e.getMessage());
            e.printStackTrace();

            // CORRECCIÓN: Devolver error como JSON
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error interno del servidor: " + e.getMessage());
            errorResponse.put("success", false);

            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, Authentication authentication) {
        Reserva reserva = reservaService.findById(id);

        if (reserva == null) {
            return ResponseEntity.notFound().build();
        }

        // Verificar que el usuario tenga acceso a eliminar esta reserva
        if (!tieneAccesoAReserva(authentication, reserva)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("mensaje", "No tiene permiso para eliminar esta reserva"));
        }

        reservaService.delete(reserva);
        return ResponseEntity.ok(Map.of("mensaje", "Reserva eliminada correctamente"));
    }

    @GetMapping("/buscar/{id}")
    public List<Reserva> buscarReservaPorUsuarioId(@PathVariable Long id) {
        return reservaService.findByUsuarioId(id);
    }

    //Remover un servicio de una reserva basado en la peticion
    @DeleteMapping("/{idReserva}/servicios/{idServicio}")
    public ResponseEntity<?> removerServicio(@PathVariable Long idReserva, @PathVariable Long idServicio, Authentication authentication) {
        Reserva reserva = reservaService.findById(idReserva);

        if (reserva == null) {
            return ResponseEntity.notFound().build();
        }

        // Verificar que el usuario tenga acceso a modificar esta reserva
        if (!tieneAccesoAReserva(authentication, reserva)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("mensaje", "No tiene permiso para modificar los servicios de esta reserva"));
        }

        reservaService.removerServicio(idReserva, idServicio);
        return ResponseEntity.ok(Map.of("mensaje", "Servicio removido correctamente"));
    }

    @PostMapping("/{idServicio}/contratar/{idHabitacion}")
    public void contratarServicio(@PathVariable Long idServicio, @PathVariable Long idHabitacion) {
        reservaService.contratarServicio(idServicio, idHabitacion);
    }

    @GetMapping("/habitaciones/disponibles/{idServicio}")
    public List<Habitacion> obtenerHabitacionesDisponibles(@PathVariable Long idServicio) {
        return reservaService.obtenerHabitacionesDisponiblesParaServicio(idServicio);
    }

    @PutMapping("/cancelar/{id}")
    public ResponseEntity<?> cancelarReserva(@PathVariable Long id, Authentication authentication) {
        Optional<Reserva> reservaOpt = reservaRepository.findById(id);

        if (reservaOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Reserva reserva = reservaOpt.get();

        // Verificar que el usuario tenga acceso a cancelar esta reserva
        if (!tieneAccesoAReserva(authentication, reserva)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("mensaje", "No tiene permiso para cancelar esta reserva"));
        }

        reserva.setEstado("Cancelada");
        reservaRepository.save(reserva);

        return ResponseEntity.ok(Map.of("mensaje", "Reserva cancelada correctamente"));
    }

    @GetMapping("/test/{id}")
    public ResponseEntity<?> testReserva(@PathVariable Long id) {
        Optional<Reserva> reservaOpt = reservaRepository.findById(id);
        if (reservaOpt.isPresent()) {
            return ResponseEntity.ok(reservaOpt.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/disponibles/servicio/{idServicio}")
    public List<Reserva> obtenerReservasDisponiblesParaServicio(@PathVariable Long idServicio) {
        return reservaService.obtenerReservasDisponiblesParaServicio(idServicio);
    }

    // ============================
    //  MÉTODOS HELPER DE SEGURIDAD
    // ============================
    /**
     * Verifica si el usuario autenticado tiene el rol de OPERADOR
     */
    private boolean isOperador(Authentication authentication) {
        if (authentication == null) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_OPERADOR"));
    }

    /**
     * Verifica si el usuario autenticado tiene acceso a una reserva específica
     * Tiene acceso si: - Es el dueño de la reserva (su correo coincide con el
     * de la reserva) - Tiene el rol de OPERADOR
     */
    private boolean tieneAccesoAReserva(Authentication authentication, Reserva reserva) {
        if (authentication == null || reserva == null || reserva.getUsuario() == null) {
            return false;
        }

        // Si es OPERADOR, tiene acceso a todas las reservas
        if (isOperador(authentication)) {
            return true;
        }

        // Si es el dueño de la reserva (comparar por correo electrónico)
        String correoAutenticado = authentication.getName();
        String correoReserva = reserva.getUsuario().getCorreo();

        return correoAutenticado != null && correoAutenticado.equals(correoReserva);
    }

}
