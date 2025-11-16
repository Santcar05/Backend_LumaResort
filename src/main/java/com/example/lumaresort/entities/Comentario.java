package com.example.lumaresort.entities;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ToString(exclude = "servicio")
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idComentario;

    private String comentario;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    private float calificacion;

    // Relación con Servicio
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "servicio_id", referencedColumnName = "idServicio")
    private Servicio servicio;

    public Comentario(String comentario, Date fecha, float calificacion, Servicio servicio) {
        this.comentario = comentario;
        this.fecha = fecha;
        this.calificacion = calificacion;
        this.servicio = servicio;
    }
}
