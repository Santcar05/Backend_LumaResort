package com.example.lumaresort.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.lumaresort.entities.Habitacion;
import com.example.lumaresort.entities.Reserva;
import com.example.lumaresort.entities.Servicio;
import com.example.lumaresort.repository.HabitacionRepository;
import com.example.lumaresort.repository.ReservaRepository;
import com.example.lumaresort.repository.ServicioRepository;

import jakarta.transaction.Transactional;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ServicioRepository servicioRepository;

    @Autowired
    private HabitacionRepository habitacionRepository;

    public java.util.List<Reserva> findAll() {
        return reservaRepository.findAll();
    }

    public Reserva save(Reserva reserva) {
        //Colocar la habitacion como ocupada
        reserva.getHabitacion().setEstado("Ocupada");
        return reservaRepository.save(reserva);
    }

    public void delete(Reserva reserva) {
        reservaRepository.delete(reserva);
    }

    public Reserva findById(Long id) {
        return reservaRepository.findById(id).orElse(null);
    }

    public List<Reserva> findByUsuarioId(Long id) {
        return reservaRepository.findByUsuarioIdUsuario(id);
    }

    @Transactional
    public void removerServicio(Long idReserva, Long idServicio) {
        Reserva reserva = reservaRepository.findById(idReserva).orElse(null);
        if (reserva != null) {
            // Buscar el servicio con referencia persistente
            Servicio servicioAEliminar = reserva.getServicios()
                    .stream()
                    .filter(s -> s.getIdServicio().equals(idServicio))
                    .findFirst()
                    .orElse(null);

            if (servicioAEliminar != null) {
                reserva.getServicios().remove(servicioAEliminar);

                // Esta línea fuerza a Hibernate a sincronizar la tabla intermedia
                reservaRepository.saveAndFlush(reserva);
            }
        }
    }

    @Transactional
    public Reserva contratarServicio(Long idServicio, Long idHabitacion) {
        // Cambia esto:
        // Reserva reserva = reservaRepository.findByHabitacionIdHabitacion(idHabitacion);

        // Por esto (toma la primera reserva de la lista):
        List<Reserva> reservas = reservaRepository.findByHabitacionIdHabitacion(idHabitacion);
        Reserva reserva = reservas.isEmpty() ? null : reservas.get(0);

        Servicio servicio = servicioRepository.findById(idServicio).orElse(null);

        if (reserva == null) {
            throw new RuntimeException("No se encontró la reserva con idHabitacion de ID: " + idHabitacion);
        }

        if (servicio == null) {
            throw new RuntimeException("No se encontró el servicio con ID: " + idServicio);
        }

        // Inicializar la lista de servicios si está vacía
        if (reserva.getServicios() == null) {
            reserva.setServicios(new ArrayList<>());
        }

        reserva.getServicios().add(servicio);
        return reservaRepository.save(reserva);
    }

    //Habitaciones que están reservadas pero sin el servicio indicado
    public List<Habitacion> obtenerHabitacionesDisponiblesParaServicio(Long idServicio) {
        List<Reserva> reservasServicio = reservaRepository.findByServiciosIdServicio(idServicio);
        List<Reserva> reservasTotales = reservaRepository.findAll();

        List<Habitacion> resultados = new ArrayList<>();

        boolean existeServicio = false;
        for (Reserva r : reservasTotales) {
            for (Reserva r2 : reservasServicio) {
                if (r.getHabitacion().getIdHabitacion().equals(r2.getHabitacion().getIdHabitacion())) {
                    existeServicio = true;
                    break; // Agregar break para optimizar
                }
            }
            if (!existeServicio) {
                resultados.add(r.getHabitacion());
            }
            existeServicio = false;
        }

        return resultados;
    }

    public List<Reserva> obtenerReservasDisponiblesParaServicio(Long idServicio) {
        // Buscar todas las reservas que ya tienen ese servicio contratado
        List<Reserva> reservasConServicio = reservaRepository.findByServiciosIdServicio(idServicio);

        // Buscar todas las reservas activas
        List<Reserva> todasLasReservas = reservaRepository.findAll();

        // Filtrar las reservas que no tienen el servicio indicado y que no están canceladas
        List<Reserva> reservasDisponibles = new ArrayList<>();
        for (Reserva r : todasLasReservas) {
            boolean tieneServicio = false;

            // Revisar si esta reserva ya tiene el servicio
            for (Reserva r2 : reservasConServicio) {
                if (r.getIdReserva().equals(r2.getIdReserva())) {
                    tieneServicio = true;
                    break;
                }
            }

            if (!tieneServicio && !"Cancelada".equalsIgnoreCase(r.getEstado())) {
                reservasDisponibles.add(r);
            }
        }

        return reservasDisponibles;
    }

}
