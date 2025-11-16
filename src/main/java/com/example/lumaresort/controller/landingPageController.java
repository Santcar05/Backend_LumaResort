package com.example.lumaresort.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.lumaresort.entities.Servicio;
import com.example.lumaresort.entities.Usuario;
import com.example.lumaresort.service.HabitacionService;
import com.example.lumaresort.service.ReservaService;
import com.example.lumaresort.service.ServicioService;
import com.example.lumaresort.service.TipoHabitacionService;
import com.example.lumaresort.service.UsuarioService;

@RestController
@CrossOrigin(origins = "*")
public class landingPageController {

    @Autowired
    private Usuario usuario;

    //Atributos de la clase
    @Autowired
    private ServicioService servicioService;
    @Autowired
    private TipoHabitacionService tipoHabitacionService;

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private HabitacionService habitacionService;

    @Autowired
    private UsuarioService usuarioService;

    //http://localhost:8080
    @GetMapping("/actividades")
    public List<Servicio> actividades(Model model) {
        //model.addAttribute("usuario", usuario);
        //Enviar todos los servicios a la vista de Angular
        //model.addAttribute("servicios", servicioService.findAll());

        return servicioService.findAll();
    }

    /* 
    @GetMapping("/Reservas")
    public List<Reserva> reservas(Model model) {

        return reservaService.findAll();
    }

     */
    @GetMapping("/usuarios")
    public List<Usuario> usuarios(Model model) {

        return usuarioService.findAll();
    }

    @GetMapping("/logout")
    public String logout() {
        usuario = null;
        return "redirect:/";
    }

    @GetMapping("/salir")
    public String salir(Model model) {
        //model.addAttribute("usuario", new Usuario());

        return "index";
    }

    /* 
    @DeleteMapping("/unaURL")
    public void  unaURL() {
        //Llamar a un servicio para eliminar algo
    }
     */

 /*
     PutMapping("/unaURL", @RequestBody Tipo nombreObjeto) //Requestbody
     , ahora el objeto viene en el cuerpo de la peticion
     public void  unaURL() {
        //Llamar a un servicio para editar algo
    }
     */
}
