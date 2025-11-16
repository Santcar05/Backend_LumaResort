package com.example.lumaresort.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.example.lumaresort.entities.Cliente;
import com.example.lumaresort.repository.ClienteRepository;

@Controller
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public void save(Cliente cliente) {
        clienteRepository.save(cliente);
    }

    public void delete(Long id) {
        clienteRepository.deleteById(id);
    }

    public Cliente findById(Long id) {
        return clienteRepository.findById(id).orElse(null);
    }

    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    public Cliente guardarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public void borrarCliente(Long id) {
        clienteRepository.deleteById(id);
    }

    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id).orElse(null);
    }

}
