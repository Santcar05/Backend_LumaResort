package com.example.lumaresort.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.lumaresort.entities.Servicio;
import com.example.lumaresort.repository.ServicioRepository;

@Service
public class ServicioService {

    @Autowired
    private ServicioRepository servicioRepository;

    public ServicioService(ServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    public List<Servicio> findAll() {
        return servicioRepository.findAll();
    }

    public Optional<Servicio> findById(Long id) {
        return servicioRepository.findById(id);
    }

    public Servicio save(Servicio servicio) {
        return servicioRepository.save(servicio);
    }

    public Servicio update(Long id, Servicio servicio) {
        Servicio servicioExistente = servicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado con id: " + id));

        servicioExistente.setNombre(servicio.getNombre());
        servicioExistente.setDescripcion(servicio.getDescripcion());
        servicioExistente.setTipo(servicio.getTipo());
        servicioExistente.setPrecio(servicio.getPrecio());
        servicioExistente.setImagenURL(servicio.getImagenURL());

        return servicioRepository.save(servicioExistente);
    }

    public void delete(Long id) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado con id: " + id));

        // Borrar dependencias si existen
        servicioRepository.delete(servicio);
    }
}
