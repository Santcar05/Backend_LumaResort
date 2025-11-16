package com.example.lumaresort.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.lumaresort.entities.Habitacion;

@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {

}
