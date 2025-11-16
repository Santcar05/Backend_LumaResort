package com.example.lumaresort.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.lumaresort.entities.Reserva;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByUsuarioIdUsuario(Long idUsuario);

    List<Reserva> findByHabitacionIdHabitacion(Long idHabitacion);

    List<Reserva> findByServiciosIdServicio(Long idServicio);

}
