package com.example.lumaresort.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Operador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idOperador;

    // MODIFICADO: Cambiado a @OneToOne con cascade
    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idUsuario", nullable = false, unique = true)
    private Usuario usuario;

    public Operador(int i, Usuario usuario) {
        this.idOperador = i;
        this.usuario = usuario;
    }
}