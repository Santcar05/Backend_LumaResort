package com.example.lumaresort.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.lumaresort.entities.Usuario;
import com.example.lumaresort.service.UsuarioService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private UsuarioService clienteService;

    @Autowired
    private UsuarioService usuarioService;

    //http://localhost:8080/clientes  (GET)
    @GetMapping
    public List<Usuario> listarClientes() {
        return clienteService.findAll();

    }

    //http://localhost:8080/clientes  (POST)
    @PostMapping
    public void guardarCliente(@RequestBody Usuario cliente) {
        clienteService.save(cliente);
    }

    //http://localhost:8080/clientes/{id}  (PUT)
    @PutMapping("/{id}")
    public Usuario actualizarCliente(@PathVariable Long id, @RequestBody Usuario cliente) {
        cliente.setIdUsuario(id);
        return clienteService.actualizar(cliente);
    }

    //http://localhost:8080/clientes/{id}  (DELETE)
    @DeleteMapping("/{id}")
    public void eliminarCliente(@PathVariable Long id) {
        clienteService.eliminar(id);
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Long idUsuario) {
        Usuario usuario = usuarioService.buscarPorId(idUsuario);
        
        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        } else { 
            return ResponseEntity.notFound().build();
        }
    }
}
