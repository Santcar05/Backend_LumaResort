package com.example.lumaresort.entities;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPago;

    private float monto;
    private Date fecha;
    private String estado;

    /*@JsonIgnore
    @ManyToOne
    private CuentaHabitacion cuentaHabitacion;
     */
    private String metodoPago;

    @OneToOne
    private Reserva reserva;
}
