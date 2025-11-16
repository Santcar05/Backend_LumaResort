package com.example.lumaresort.service;

import com.example.lumaresort.dto.*;
import com.example.lumaresort.entities.Habitacion;
import com.example.lumaresort.entities.Reserva;
import com.example.lumaresort.entities.Usuario;
import com.example.lumaresort.repository.HabitacionRepository;
import com.example.lumaresort.repository.ReservaRepository;
import com.example.lumaresort.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private HabitacionRepository habitacionRepository;

    /**
     * Obtiene las estadísticas generales del dashboard
     */
    public DashboardEstadisticas obtenerEstadisticasGenerales() {
        List<Reserva> todasReservas = reservaRepository.findAll();
        List<Usuario> todosUsuarios = usuarioRepository.findAll();
        List<Habitacion> todasHabitaciones = habitacionRepository.findAll();

        // Calcular ingresos del mes actual
        int mesActual = LocalDate.now().getMonthValue();
        int anioActual = LocalDate.now().getYear();

        double ingresosMesActual = todasReservas.stream()
                .filter(reserva -> {
                    if (reserva.getFechaInicio() == null) {
                        return false;
                    }
                    LocalDate fecha = reserva.getFechaInicio().toInstant()
                            .atZone(ZoneId.systemDefault()).toLocalDate();
                    return fecha.getMonthValue() == mesActual
                            && fecha.getYear() == anioActual
                            && (reserva.getEstado().equalsIgnoreCase("CONFIRMADA")
                            || reserva.getEstado().equalsIgnoreCase("COMPLETADA"));
                })
                .mapToDouble(this::calcularPrecioReserva)
                .sum();

        double ocupacionActual = calcularOcupacionActual();

        return DashboardEstadisticas.builder()
                .totalReservas((long) todasReservas.size())
                .totalUsuarios((long) todosUsuarios.size())
                .totalHabitaciones((long) todasHabitaciones.size())
                .ocupacionActual(ocupacionActual)
                .ingresosMes(ingresosMesActual)
                .build();
    }

    /**
     * Obtiene el número de reservas por mes del año actual
     */
    public ReservasPorMes obtenerReservasPorMes() {
        List<Reserva> reservas = reservaRepository.findAll();
        int anioActual = LocalDate.now().getYear();

        Integer[] reservasPorMes = new Integer[12];
        Arrays.fill(reservasPorMes, 0);

        for (Reserva reserva : reservas) {
            if (reserva.getFechaInicio() != null) {
                LocalDate fecha = reserva.getFechaInicio().toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDate();

                if (fecha.getYear() == anioActual) {
                    int mes = fecha.getMonthValue() - 1; // 0-11
                    reservasPorMes[mes]++;
                }
            }
        }

        return new ReservasPorMes(Arrays.asList(reservasPorMes));
    }

    /**
     * Obtiene el número de reservas agrupadas por estado
     */
    public ReservasPorEstado obtenerReservasPorEstado() {
        List<Reserva> reservas = reservaRepository.findAll();

        Map<String, Long> estadosCuenta = reservas.stream()
                .collect(Collectors.groupingBy(
                        reserva -> reserva.getEstado() != null ? reserva.getEstado().toUpperCase() : "PENDIENTE",
                        Collectors.counting()
                ));

        return new ReservasPorEstado(estadosCuenta);
    }

    /**
     * Obtiene los ingresos mensuales del año actual
     */
    public IngresosPorMes obtenerIngresosPorMes() {
        List<Reserva> reservas = reservaRepository.findAll();
        int anioActual = LocalDate.now().getYear();

        Double[] ingresosPorMes = new Double[12];
        Arrays.fill(ingresosPorMes, 0.0);

        for (Reserva reserva : reservas) {
            if (reserva.getFechaInicio() != null
                    && (reserva.getEstado().equalsIgnoreCase("CONFIRMADA")
                    || reserva.getEstado().equalsIgnoreCase("COMPLETADA"))) {

                LocalDate fecha = reserva.getFechaInicio().toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDate();

                if (fecha.getYear() == anioActual) {
                    int mes = fecha.getMonthValue() - 1;
                    double precio = calcularPrecioReserva(reserva);
                    ingresosPorMes[mes] += precio;
                }
            }
        }

        return new IngresosPorMes(Arrays.asList(ingresosPorMes));
    }

    /**
     * Obtiene los ingresos agrupados por tipo de habitación
     */
    public IngresosPorTipo obtenerIngresosPorTipo() {
        List<Reserva> reservas = reservaRepository.findAll();

        Map<String, Double> ingresosPorTipo = new HashMap<>();

        for (Reserva reserva : reservas) {
            if (reserva.getEstado().equalsIgnoreCase("CONFIRMADA")
                    || reserva.getEstado().equalsIgnoreCase("COMPLETADA")) {

                String tipoHabitacion = "Sin tipo";
                if (reserva.getHabitacion() != null
                        && reserva.getHabitacion().getTipoHabitacion() != null) {
                    tipoHabitacion = reserva.getHabitacion().getTipoHabitacion().getNombre();
                }

                double precio = calcularPrecioReserva(reserva);
                ingresosPorTipo.put(tipoHabitacion,
                        ingresosPorTipo.getOrDefault(tipoHabitacion, 0.0) + precio);
            }
        }

        return new IngresosPorTipo(ingresosPorTipo);
    }

    /**
     * Obtiene la distribución de habitaciones por tipo
     */
    public TiposHabitacionDTO obtenerDistribucionTiposHabitacion() {
        List<Habitacion> habitaciones = habitacionRepository.findAll();

        Map<String, Long> distribucion = habitaciones.stream()
                .collect(Collectors.groupingBy(
                        habitacion -> habitacion.getTipoHabitacion() != null
                        ? habitacion.getTipoHabitacion().getNombre()
                        : "Sin tipo",
                        Collectors.counting()
                ));

        return new TiposHabitacionDTO(distribucion);
    }

    /**
     * Obtiene el número de usuarios registrados por mes del año actual
     */
    public UsuariosPorMes obtenerUsuariosPorMes() {
        // Nota: esto requiere que Usuario tenga un campo fechaRegistro
        // Si no existe, tendrás que agregarlo o simular datos
        List<Usuario> usuarios = usuarioRepository.findAll();
        int anioActual = LocalDate.now().getYear();

        Integer[] usuariosPorMes = new Integer[12];
        Arrays.fill(usuariosPorMes, 0);

        // Si tus usuarios no tienen fecha de registro, este código no funcionará correctamente
        // Tendrías que agregar un campo fechaRegistro a la entidad Usuario
        return new UsuariosPorMes(Arrays.asList(usuariosPorMes));
    }

    /**
     * Calcula el porcentaje de ocupación actual del hotel
     */
    public Double calcularOcupacionActual() {
        List<Habitacion> todasHabitaciones = habitacionRepository.findAll();
        List<Reserva> reservasActivas = reservaRepository.findAll().stream()
                .filter(reserva -> {
                    if (reserva.getFechaInicio() == null || reserva.getFechaFin() == null) {
                        return false;
                    }
                    Date hoy = new Date();
                    return (reserva.getEstado().equalsIgnoreCase("CONFIRMADA")
                            || reserva.getEstado().equalsIgnoreCase("EN_PROCESO"))
                            && !reserva.getFechaInicio().after(hoy)
                            && !reserva.getFechaFin().before(hoy);
                })
                .collect(Collectors.toList());

        if (todasHabitaciones.isEmpty()) {
            return 0.0;
        }

        double porcentaje = (double) reservasActivas.size() / todasHabitaciones.size() * 100;
        return Math.round(porcentaje * 100.0) / 100.0; // Redondear a 2 decimales
    }

    /**
     * Obtiene la ocupación por tipo de habitación
     */
    public Map<String, Double> obtenerOcupacionPorTipo() {
        List<Habitacion> todasHabitaciones = habitacionRepository.findAll();
        List<Reserva> reservasActivas = reservaRepository.findAll().stream()
                .filter(reserva -> {
                    if (reserva.getFechaInicio() == null || reserva.getFechaFin() == null) {
                        return false;
                    }
                    Date hoy = new Date();
                    return (reserva.getEstado().equalsIgnoreCase("CONFIRMADA")
                            || reserva.getEstado().equalsIgnoreCase("EN_PROCESO"))
                            && !reserva.getFechaInicio().after(hoy)
                            && !reserva.getFechaFin().before(hoy);
                })
                .collect(Collectors.toList());

        Map<String, Long> habitacionesPorTipo = todasHabitaciones.stream()
                .collect(Collectors.groupingBy(
                        h -> h.getTipoHabitacion() != null
                        ? h.getTipoHabitacion().getNombre()
                        : "Sin tipo",
                        Collectors.counting()
                ));

        Map<String, Long> ocupadasPorTipo = reservasActivas.stream()
                .filter(r -> r.getHabitacion() != null && r.getHabitacion().getTipoHabitacion() != null)
                .collect(Collectors.groupingBy(
                        r -> r.getHabitacion().getTipoHabitacion().getNombre(),
                        Collectors.counting()
                ));

        Map<String, Double> ocupacionPorTipo = new HashMap<>();
        for (Map.Entry<String, Long> entry : habitacionesPorTipo.entrySet()) {
            String tipo = entry.getKey();
            Long total = entry.getValue();
            Long ocupadas = ocupadasPorTipo.getOrDefault(tipo, 0L);
            double porcentaje = total > 0 ? (double) ocupadas / total * 100 : 0.0;
            ocupacionPorTipo.put(tipo, Math.round(porcentaje * 100.0) / 100.0);
        }

        return ocupacionPorTipo;
    }

    /**
     * Calcula el precio total de una reserva
     */
    private double calcularPrecioReserva(Reserva reserva) {
        if (reserva.getHabitacion() == null || reserva.getHabitacion().getTipoHabitacion() == null) {
            return 0.0;
        }

        if (reserva.getFechaInicio() == null || reserva.getFechaFin() == null) {
            return 0.0;
        }

        // Calcular días de estadía
        long diferenciaMilis = reserva.getFechaFin().getTime() - reserva.getFechaInicio().getTime();
        long dias = diferenciaMilis / (1000 * 60 * 60 * 24);
        if (dias <= 0) {
            dias = 1;
        }

        // Precio base de la habitación
        double precioBase = reserva.getHabitacion().getTipoHabitacion().getPrecio() * dias;

        // Agregar precio de servicios adicionales
        // CORREGIDO: El precio es float primitivo, no puede ser null
        double precioServicios = 0.0;
        if (reserva.getServicios() != null && !reserva.getServicios().isEmpty()) {
            precioServicios = reserva.getServicios().stream()
                    .mapToDouble(servicio -> (double) servicio.getPrecio())
                    .sum();
        }

        return precioBase + precioServicios;
    }
}
