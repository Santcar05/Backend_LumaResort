package com.example.lumaresort.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.lumaresort.entities.Pago;
import com.example.lumaresort.repository.PagoRepository;

@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    public void guardarPago(Pago pago) {
        pagoRepository.save(pago);
    }

    public List<Pago> listarPagos() {
        return pagoRepository.findAll();
    }
}
