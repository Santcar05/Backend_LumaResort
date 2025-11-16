package com.example.lumaresort.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.lumaresort.entities.Servicio;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Long> {

    // Método para buscar servicios por nombre (contiene, sin distinguir mayúsculas/minúsculas)
    List<Servicio> findByNombreContainingIgnoreCase(String nombre);

    @Query(value = "SELECT * FROM servicio WHERE precio > :precioMinimo", nativeQuery = true)
    List<Servicio> buscarServiciosCaros(float precioMinimo);

}
