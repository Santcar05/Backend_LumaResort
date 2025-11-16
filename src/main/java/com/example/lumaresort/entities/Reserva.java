package com.example.lumaresort.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReserva;

    @Temporal(TemporalType.DATE)
    private Date fechaInicio;

    @Temporal(TemporalType.DATE)
    private Date fechaFin;

    private Integer cantidadPersonas;

    private String estado;

    // Relación con Cliente: muchas reservas pueden pertenecer a un cliente
    @JsonIgnoreProperties({"reservas", "historial", "cliente", "administrador", "operador"})
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @JsonIgnoreProperties({"reservas"})
    @ManyToOne
    @JoinColumn(name = "habitacion_id")
    private Habitacion habitacion;

    //Conjunto de servicios
    @JsonIgnoreProperties({"reservas"})
    @ManyToMany
    @JoinTable(
            name = "reserva_servicio",
            joinColumns = @JoinColumn(name = "reserva_id"),
            inverseJoinColumns = @JoinColumn(name = "servicio_id")
    )
    @Builder.Default
    private List<Servicio> servicios = new ArrayList<>();

    public Reserva(Date fechaInicio, Date fechaFin, Integer cantidadPersonas, String estado, Usuario cliente, Habitacion habitacion) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.cantidadPersonas = cantidadPersonas;
        this.estado = estado;
        this.usuario = cliente;
        this.habitacion = habitacion;
        this.servicios = new ArrayList<>();
    }

    public Reserva(Date fechaInicio, Date fechaFin, Integer cantidadPersonas, String estado, Usuario cliente, Habitacion habitacion, List<Servicio> servicios) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.cantidadPersonas = cantidadPersonas;
        this.estado = estado;
        this.usuario = cliente;
        this.habitacion = habitacion;
        this.servicios = servicios;
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "idReserva=" + idReserva +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", cantidadPersonas=" + cantidadPersonas +
                ", estado='" + estado + '\'' +
                ", habitacionId=" + (habitacion != null ? habitacion.getIdHabitacion() : null) +
                ", usuarioId=" + (usuario != null ? usuario.getIdUsuario() : null) +
                ", serviciosCount=" + (servicios != null ? servicios.size() : 0) +
                '}';
    }
}