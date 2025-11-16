package com.example.lumaresort.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.lumaresort.entities.TipoHabitacion;

@Repository
public interface TipoHabitacionRepository extends JpaRepository<TipoHabitacion, Long> {

}
