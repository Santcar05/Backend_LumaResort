package com.example.lumaresort.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardEstadisticas {

    private Long totalReservas;
    private Long totalUsuarios;
    private Long totalHabitaciones;
    private Double ocupacionActual;
    private Double ingresosMes;
}
