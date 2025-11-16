package com.example.lumaresort.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

import com.example.lumaresort.entities.Habitacion;
import com.example.lumaresort.entities.Reserva;
import com.example.lumaresort.repository.ReservaRepository;
import com.example.lumaresort.service.HabitacionService;

@RestController
@CrossOrigin(origins = "*")
public class HabitacionController {

    @Autowired
    private HabitacionService habitacionService;

    @Autowired
    private ReservaRepository reservaRepository;

    @RequestMapping("/habitaciones")
    @GetMapping
    public ResponseEntity<List<Habitacion>> listarHabitaciones() {

        List<Habitacion> habitaciones = habitacionService.listarTodos();
        ResponseEntity<List<Habitacion>> response = ResponseEntity.ok(habitaciones);
        return response;
    }

    @GetMapping("/habitaciones/{id}")
    public ResponseEntity<Habitacion> obtenerHabitacion(@PathVariable Long id) {
        Habitacion habitacion = habitacionService.buscarHabitacionPorId(id);
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(habitacion);
    }

    @PostMapping("/habitaciones")
    public ResponseEntity<Habitacion> crearHabitacion(@RequestBody Habitacion habitacion) {
        if (habitacion.getTipoHabitacion() == null || habitacion.getTipoHabitacion().getId() == null) {
            throw new RuntimeException("Debe especificar el tipo de habitación");
        }

        Habitacion habitacionCreada = habitacionService.crearHabitacion(habitacion, habitacion.getTipoHabitacion().getId());

        return ResponseEntity.ok(habitacionCreada);
    }

    @PutMapping("/habitaciones/{id}")
    public ResponseEntity<Habitacion> actualizarHabitacion(@PathVariable Long id, @RequestBody Habitacion habitacion) {
        if (habitacion.getTipoHabitacion() == null || habitacion.getTipoHabitacion().getId() == null) {
            throw new RuntimeException("Debe especificar el tipo de habitación");
        }
        habitacionService.actualizarHabitacion(id, habitacion, habitacion.getTipoHabitacion().getId());
        Habitacion habitacionActualizada = habitacionService.buscarHabitacionPorId(id);

        return ResponseEntity.ok(habitacionActualizada);
    }

    @DeleteMapping("/habitaciones/{id}")
    public ResponseEntity<String> eliminarHabitacion(@PathVariable Long id) {
        habitacionService.eliminarHabitacion(id);
        return new ResponseEntity<>("Habitación eliminada correctamente", HttpStatus.NO_CONTENT);
    }

    /**
     * Endpoint para obtener habitaciones disponibles en un rango de fechas
     *
     * @param fechaInicio fecha de inicio en formato yyyy-MM-dd
     * @param fechaFin fecha de fin en formato yyyy-MM-dd
     * @return lista de habitaciones disponibles en ese rango de fechas
     */
    @GetMapping("/habitaciones/disponibles")
    public ResponseEntity<?> obtenerHabitacionesDisponibles(
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin) {

        try {
            // Parsear las fechas recibidas
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date inicio = formatter.parse(fechaInicio);
            Date fin = formatter.parse(fechaFin);

            // Obtener todas las habitaciones disponibles
            List<Habitacion> todasHabitaciones = habitacionService.listarTodos()
                    .stream()
                    .filter(h -> "Disponible".equals(h.getEstado()))
                    .collect(Collectors.toList());

            // Obtener todas las reservas activas (no canceladas)
            List<Reserva> reservasActivas = reservaRepository.findAll()
                    .stream()
                    .filter(r -> !"Cancelada".equalsIgnoreCase(r.getEstado()))
                    .collect(Collectors.toList());

            // Filtrar habitaciones que NO tienen reservas en el rango de fechas
            List<Habitacion> habitacionesDisponibles = todasHabitaciones.stream()
                    .filter(habitacion -> {
                        // Verificar si la habitación tiene alguna reserva que se solape con las fechas
                        boolean tieneReserva = reservasActivas.stream()
                                .anyMatch(reserva -> {
                                    // Solo verificar reservas de esta habitación
                                    if (reserva.getHabitacion() == null
                                            || !reserva.getHabitacion().getIdHabitacion().equals(habitacion.getIdHabitacion())) {
                                        return false;
                                    }

                                    // Verificar si las fechas se solapan
                                    // Las fechas se solapan si: inicio <= reservaFin AND fin >= reservaInicio
                                    return !inicio.after(reserva.getFechaFin()) && !fin.before(reserva.getFechaInicio());
                                });

                        // Si NO tiene reserva en ese rango, está disponible
                        return !tieneReserva;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(habitacionesDisponibles);

        } catch (ParseException e) {
            return ResponseEntity.badRequest()
                    .body("Error al parsear las fechas. Use formato yyyy-MM-dd");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener habitaciones disponibles: " + e.getMessage());
        }
    }

}
