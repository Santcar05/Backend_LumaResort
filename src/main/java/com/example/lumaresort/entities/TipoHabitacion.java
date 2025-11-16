package com.example.lumaresort.entities;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ToString(exclude = {"habitaciones"})
public class TipoHabitacion {

    @Id
    @GeneratedValue
    private Long id;

    private String nombre;
    private String descripcion;

    private List<String> imagenes;
    private List<String> caracteristicas;
    private Double precio;

    @JsonIgnore
    @OneToMany(mappedBy = "tipoHabitacion")
    @Builder.Default
    private List<Habitacion> habitaciones = new ArrayList<>();
}
