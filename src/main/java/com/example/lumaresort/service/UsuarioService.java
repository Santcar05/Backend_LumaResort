package com.example.lumaresort.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.lumaresort.entities.Usuario;
import com.example.lumaresort.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public void save(Usuario usuario) {
        usuario.setIdUsuario(null);
        usuarioRepository.save(usuario);
    }

    public Usuario findByCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    public Usuario findByCorreoAndContrasena(String correo, String contrasena) {
        return usuarioRepository.findByCorreoAndContrasena(correo, contrasena);
    }

    public Usuario buscarPorId(Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        return usuario.orElse(null);
    }

    // Buscar usuario por correo
    public Usuario buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    // Actualizar usuario existente
    public Usuario actualizar(Usuario usuario) {
        if (usuario.getIdUsuario() != null && usuarioRepository.existsById(usuario.getIdUsuario())) {
            return usuarioRepository.save(usuario);
        }
        throw new RuntimeException("Usuario no encontrado");
    }

    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Usuario login(String correo, String contrasena) {
        return usuarioRepository.findByCorreoAndContrasena(correo, contrasena);
        //Buscar por corr
    }
}
