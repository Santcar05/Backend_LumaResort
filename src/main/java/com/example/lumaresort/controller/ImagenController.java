package com.example.lumaresort.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ImagenController {

    private List<String> imagenes = new ArrayList<>();

    @PostMapping("/imagenes/agregar")
    public String agregarImagen(@RequestParam("url") String url, RedirectAttributes redirectAttributes) {
        imagenes.add(url);
        redirectAttributes.addFlashAttribute("mensaje", "Imagen agregada correctamente!");
        return "redirect:/habitaciones/detalle";
    }

    // Método para enviar la lista de imágenes al modelo
    public List<String> getImagenes() {
        return imagenes;
    }
}
