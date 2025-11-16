package com.example.lumaresort.dto;

import java.util.Date;

public class ActualizarReservaRequest {
    private Date fechaInicio;
    private Date fechaFin;
    private Integer cantidadPersonas;
    private String estado;
    private Long idUsuario;
    private Long idHabitacion;

    // Constructor vacío
    public ActualizarReservaRequest() {}

    // Constructor con parámetros
    public ActualizarReservaRequest(Date fechaInicio, Date fechaFin, Integer cantidadPersonas, 
                                   String estado, Long idUsuario, Long idHabitacion) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.cantidadPersonas = cantidadPersonas;
        this.estado = estado;
        this.idUsuario = idUsuario;
        this.idHabitacion = idHabitacion;
    }

    // Getters y Setters
    public Date getFechaInicio() { 
        return fechaInicio; 
    }
    
    public void setFechaInicio(Date fechaInicio) { 
        this.fechaInicio = fechaInicio; 
    }
    
    public Date getFechaFin() { 
        return fechaFin; 
    }
    
    public void setFechaFin(Date fechaFin) { 
        this.fechaFin = fechaFin; 
    }
    
    public Integer getCantidadPersonas() { 
        return cantidadPersonas; 
    }
    
    public void setCantidadPersonas(Integer cantidadPersonas) { 
        this.cantidadPersonas = cantidadPersonas; 
    }
    
    public String getEstado() { 
        return estado; 
    }
    
    public void setEstado(String estado) { 
        this.estado = estado; 
    }
    
    public Long getIdUsuario() { 
        return idUsuario; 
    }
    
    public void setIdUsuario(Long idUsuario) { 
        this.idUsuario = idUsuario; 
    }
    
    public Long getIdHabitacion() { 
        return idHabitacion; 
    }
    
    public void setIdHabitacion(Long idHabitacion) { 
        this.idHabitacion = idHabitacion; 
    }

    @Override
    public String toString() {
        return "ActualizarReservaRequest{" +
                "fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", cantidadPersonas=" + cantidadPersonas +
                ", estado='" + estado + '\'' +
                ", idUsuario=" + idUsuario +
                ", idHabitacion=" + idHabitacion +
                '}';
    }
}