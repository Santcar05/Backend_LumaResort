package com.example.lumaresort.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.lumaresort.entities.Cliente;
import com.example.lumaresort.entities.Usuario;
import com.example.lumaresort.service.ClienteService;
import com.example.lumaresort.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ClienteService service;

    //http://localhost:8090/login
    @GetMapping()
    public String login(Model model) {
        model.addAttribute("usuario", new Usuario()); // Crear nueva instancia
        return "login";
    }

    //http://localhost:8090/login
    @PostMapping()
    public String login(@ModelAttribute("usuario") Usuario usuarioF, Model model, HttpSession session) {
        Usuario usuarioEncontrado = usuarioService.findByCorreoAndContrasena(usuarioF.getCorreo(), usuarioF.getContrasena());
        
        if (usuarioEncontrado == null) {
            model.addAttribute("error", "Credenciales incorrectas");
            return "login"; // Mejor mostrar error que redireccionar
        }
        
        // IMPORTANTE: Guardar usuario en la sesión
        session.setAttribute("usuarioLogueado", usuarioEncontrado);
        
        if (usuarioEncontrado.isEsAdministrador()) {
            // Agregamos el objeto necesario para Thymeleaf
            model.addAttribute("nuevoCliente", new Cliente());
            model.addAttribute("clientes", service.listarClientes());
            model.addAttribute("usuarioRegistrado", usuarioEncontrado);
            
            return "clientes";
        } else {
            // Para usuarios regulares
            model.addAttribute("usuario", usuarioEncontrado);
            return "index";
        }
    }
    

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
