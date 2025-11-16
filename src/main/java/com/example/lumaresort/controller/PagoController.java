package com.example.lumaresort.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.lumaresort.entities.Pago;
import com.example.lumaresort.service.PagoService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @PostMapping
    public void guardarPago(@RequestBody Pago pago) {
        pagoService.guardarPago(pago);
    }

    @GetMapping
    public List<Pago> listarPagos() {
        return pagoService.listarPagos();
    }
}
