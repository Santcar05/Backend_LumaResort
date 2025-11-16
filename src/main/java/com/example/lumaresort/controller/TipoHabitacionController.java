package com.example.lumaresort.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.lumaresort.entities.TipoHabitacion;
import com.example.lumaresort.service.TipoHabitacionService;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class TipoHabitacionController {

    @Autowired
    private TipoHabitacionService tipoHabitacionService;

    //http://localhost:8080/clientes  (GET)
    @GetMapping("/tiposHabitacion")
    public List<TipoHabitacion> tiposHabitacion(Model model) {
        return tipoHabitacionService.findAll();
    }

    //http://localhost:8080/tiposHabitacion  (POST)
    @PostMapping("/tiposHabitacion")
    public TipoHabitacion guardarTipoHabitacion(@RequestBody TipoHabitacion tipoHabitacion, RedirectAttributes redirectAttributes) {
        TipoHabitacion nuevoTipo = tipoHabitacionService.crear(tipoHabitacion);
        redirectAttributes.addFlashAttribute("mensaje", "Tipo de habitación guardado exitosamente.");
        return nuevoTipo;
    }

    //http://localhost:8080/tiposHabitacion/{id}  (PUT)
    @PutMapping("/tiposHabitacion/{id}")
    public TipoHabitacion actualizarTipoHabitacion(@PathVariable Long id, @RequestBody TipoHabitacion tipoHabitacion, RedirectAttributes redirectAttributes) {
        TipoHabitacion tipoActualizado = tipoHabitacionService.actualizar(id, tipoHabitacion);
        redirectAttributes.addFlashAttribute("mensaje", "Tipo de habitación actualizado exitosamente.");
        return tipoActualizado;
    }

    //http://localhost:8080/tiposHabitacion/{id}  (DELETE)
    @DeleteMapping("/tiposHabitacion/{id}")
    public void eliminarTipoHabitacion(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            tipoHabitacionService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Tipo de habitación eliminado exitosamente.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
    }

}
