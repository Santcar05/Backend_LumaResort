package com.example.lumaresort.controller;

import com.example.lumaresort.dto.DashboardEstadisticas;
import com.example.lumaresort.dto.ReservasPorEstado;
import com.example.lumaresort.dto.ReservasPorMes;
import com.example.lumaresort.dto.IngresosPorMes;
import com.example.lumaresort.dto.IngresosPorTipo;
import com.example.lumaresort.dto.TiposHabitacionDTO;
import com.example.lumaresort.dto.UsuariosPorMes;
import com.example.lumaresort.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    /**
     * Obtiene las estadísticas generales del dashboard
     *
     * @return Estadísticas generales (total reservas, usuarios, habitaciones,
     * ocupación, ingresos)
     */
    @GetMapping("/estadisticas")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'OPERADOR')")
    public ResponseEntity<DashboardEstadisticas> getEstadisticasGenerales() {
        DashboardEstadisticas estadisticas = dashboardService.obtenerEstadisticasGenerales();
        return ResponseEntity.ok(estadisticas);
    }

    /**
     * Obtiene el número de reservas por mes del año actual
     *
     * @return Lista con 12 elementos (enero a diciembre)
     */
    @GetMapping("/reservas-por-mes")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'OPERADOR')")
    public ResponseEntity<ReservasPorMes> getReservasPorMes() {
        ReservasPorMes reservasPorMes = dashboardService.obtenerReservasPorMes();
        return ResponseEntity.ok(reservasPorMes);
    }

    /**
     * Obtiene el número de reservas agrupadas por estado
     *
     * @return Mapa con los estados y su cantidad
     */
    @GetMapping("/reservas-por-estado")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'OPERADOR')")
    public ResponseEntity<ReservasPorEstado> getReservasPorEstado() {
        ReservasPorEstado reservasPorEstado = dashboardService.obtenerReservasPorEstado();
        return ResponseEntity.ok(reservasPorEstado);
    }

    /**
     * Obtiene los ingresos mensuales del año actual
     *
     * @return Lista con 12 elementos (enero a diciembre)
     */
    @GetMapping("/ingresos-por-mes")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'OPERADOR')")
    public ResponseEntity<IngresosPorMes> getIngresosPorMes() {
        IngresosPorMes ingresosPorMes = dashboardService.obtenerIngresosPorMes();
        return ResponseEntity.ok(ingresosPorMes);
    }

    /**
     * Obtiene los ingresos agrupados por tipo de habitación
     *
     * @return Mapa con tipos de habitación y sus ingresos
     */
    @GetMapping("/ingresos-por-tipo")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'OPERADOR')")
    public ResponseEntity<IngresosPorTipo> getIngresosPorTipo() {
        IngresosPorTipo ingresosPorTipo = dashboardService.obtenerIngresosPorTipo();
        return ResponseEntity.ok(ingresosPorTipo);
    }

    /**
     * Obtiene la distribución de habitaciones por tipo
     *
     * @return Mapa con tipos de habitación y su cantidad
     */
    @GetMapping("/tipos-habitacion")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'OPERADOR')")
    public ResponseEntity<TiposHabitacionDTO> getTiposHabitacion() {
        TiposHabitacionDTO tiposHabitacion = dashboardService.obtenerDistribucionTiposHabitacion();
        return ResponseEntity.ok(tiposHabitacion);
    }

    /**
     * Obtiene el número de usuarios registrados por mes
     *
     * @return Lista con 12 elementos (enero a diciembre)
     */
    @GetMapping("/usuarios-por-mes")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'OPERADOR')")
    public ResponseEntity<UsuariosPorMes> getUsuariosPorMes() {
        UsuariosPorMes usuariosPorMes = dashboardService.obtenerUsuariosPorMes();
        return ResponseEntity.ok(usuariosPorMes);
    }

    /**
     * Obtiene la ocupación actual del hotel
     *
     * @return Porcentaje de ocupación actual
     */
    @GetMapping("/ocupacion-actual")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'OPERADOR')")
    public ResponseEntity<Map<String, Double>> getOcupacionActual() {
        Double ocupacion = dashboardService.calcularOcupacionActual();
        return ResponseEntity.ok(Map.of("ocupacion", ocupacion));
    }

    /**
     * Obtiene la ocupación por tipo de habitación
     *
     * @return Mapa con tipos de habitación y su porcentaje de ocupación
     */
    @GetMapping("/ocupacion-por-tipo")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'OPERADOR')")
    public ResponseEntity<Map<String, Double>> getOcupacionPorTipo() {
        Map<String, Double> ocupacionPorTipo = dashboardService.obtenerOcupacionPorTipo();
        return ResponseEntity.ok(ocupacionPorTipo);
    }
}
