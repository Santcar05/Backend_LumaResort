package com.example.lumaresort.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.lumaresort.entities.Habitacion;
import com.example.lumaresort.entities.TipoHabitacion;
import com.example.lumaresort.repository.HabitacionRepository;

@Service
public class HabitacionService {

    @Autowired
    private HabitacionRepository habitacionRepository;

    @Autowired
    private TipoHabitacionService tipoHabitacionService;

    public Habitacion crearHabitacion(Habitacion habitacion, Long tipoHabitacionId) {
        TipoHabitacion tipoHabitacion = tipoHabitacionService.buscarPorId(tipoHabitacionId).orElseThrow(
                () -> new RuntimeException("TipoHabitacion no encontrado con id: " + tipoHabitacionId)
        );
        habitacion.setTipoHabitacion(tipoHabitacion);
        return habitacionRepository.save(habitacion);
    }

    public Habitacion buscarHabitacionPorId(Long id) {
        return habitacionRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Habitación no encontrada con id: " + id)
        );
    }

    public void eliminarHabitacion(Long id) {
        Habitacion habitacion = habitacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Habitación no encontrada con id: " + id));

        // Si necesitas hacer algo antes de borrar, hazlo aquí
        habitacionRepository.delete(habitacion);
    }

    public void actualizarHabitacion(Long habitacionId, Habitacion habitacion, Long tipoId) {
        Habitacion habitacionExistente = buscarHabitacionPorId(habitacionId);
        TipoHabitacion tipoHabitacion = tipoHabitacionService.buscarPorId(tipoId).orElseThrow(
                () -> new RuntimeException("TipoHabitacion no encontrado con id: " + tipoId)
        );

        habitacionExistente.setNumero(habitacion.getNumero());
        habitacionExistente.setPrecioPorNoche(habitacion.getPrecioPorNoche());
        habitacionExistente.setEstado(habitacion.getEstado());
        habitacionExistente.setCapacidad(habitacion.getCapacidad());
        habitacionExistente.setDescripcion(habitacion.getDescripcion());
        habitacionExistente.setTipoHabitacion(tipoHabitacion);

        habitacionRepository.save(habitacionExistente);
    }

    public List<Habitacion> listarTodos() {
        return habitacionRepository.findAll();
    }
}
