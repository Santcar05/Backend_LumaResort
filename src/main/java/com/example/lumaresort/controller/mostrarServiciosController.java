package com.example.lumaresort.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.lumaresort.entities.Servicio;
import com.example.lumaresort.service.ServicioService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/servicios")
public class mostrarServiciosController {

    @Autowired
    private ServicioService servicioService;

    @GetMapping
    public List<Servicio> listarServicios() {
        return servicioService.findAll();
    }

    @GetMapping("/{id}")
    public Servicio obtenerServicio(@PathVariable Long id) {
        return servicioService.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado con id: " + id));
    }

    @PostMapping
    public Servicio crearServicio(@RequestBody Servicio servicio) {
        servicio.setIdServicio(null);
        return servicioService.save(servicio);
    }

    @PutMapping("/{id}")
    public Servicio actualizarServicio(@PathVariable Long id, @RequestBody Servicio servicio) {
        return servicioService.update(id, servicio);
    }

    @DeleteMapping("/{id}")
    public void eliminarServicio(@PathVariable Long id) {
        servicioService.delete(id);
    }
}
