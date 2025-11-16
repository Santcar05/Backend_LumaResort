package com.example.lumaresort.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
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
@ToString(exclude = "comentarios")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idServicio;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String descripcion;
    private String nombre;
    private String tipo;
    private float precio;
    private String imagenURL;

    // Un servicio puede tener varios comentarios
    @OneToMany(mappedBy = "servicio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comentario> comentarios;

    public Servicio(String nombre, String tipo, String descripcion, float precio, String imagenURL) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imagenURL = imagenURL;
    }

    public Servicio(String nombre, String descripcion, float precio, String imagenURL, List<Comentario> comentarios) {
        this.comentarios = comentarios;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imagenURL = imagenURL;
    }
}
