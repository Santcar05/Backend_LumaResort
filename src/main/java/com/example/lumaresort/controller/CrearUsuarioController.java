package com.example.lumaresort.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.lumaresort.Utils.EmailService;
import com.example.lumaresort.entities.Usuario;
import com.example.lumaresort.service.UsuarioService;

@RequestMapping("/crearUsuario")
@Controller
public class CrearUsuarioController {

    @Autowired
    private Usuario usuarioCrearCuenta;

    @Autowired
    private UsuarioService usuarioService;

    @Value("${correo.app}")
    private String correo;

    @Value("${correo.password}")
    private String contrasena;

    //localhost:8080/crearUsuario
    @GetMapping()
    public String login(Model model) {
        model.addAttribute("usuarioCrearCuenta", usuarioCrearCuenta);
        return "crearUsuario";
    }

    @PostMapping()
    public String crearCuenta(@ModelAttribute("usuarioCrearCuenta") Usuario usuarioF) {
        Usuario usuarioEncontrado = usuarioService.findByCorreo(correo);

        if (usuarioEncontrado != null) {
            return "index"; // Redirigir a la página de inicio si el usuario ya existe
        }

        if (usuarioF.getCorreo() == null || usuarioF.getContrasena() == null) {
            return "index";
        }

        usuarioService.save(usuarioF);

        this.usuarioCrearCuenta = usuarioF;
        EmailService emailService = new EmailService(correo, contrasena);
        // Enviar correo de verificación (lógica simplificada)
        emailService.enviarCorreo(usuarioF.getCorreo(), "Verificación de cuenta", "Gracias por crear una cuenta. Es muy importante para nosotros tener la oportunidad de tener nuevos usuarios en nuestra plataforma.");
        return "index";
    }
}
