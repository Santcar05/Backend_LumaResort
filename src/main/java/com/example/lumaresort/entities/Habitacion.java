package com.example.lumaresort.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ToString(exclude = {"tipoHabitacion"})
public class Habitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHabitacion;

    private String numero;
    private float precioPorNoche;
    private String estado;
    private Integer capacidad;
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "idTipoHabitacion")
    private TipoHabitacion tipoHabitacion;
}
