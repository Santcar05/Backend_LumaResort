package com.example.lumaresort.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clima")
@CrossOrigin(origins = "http://localhost:4200")
public class ClimaController {

    @GetMapping
    public ResponseEntity<Map<String, Object>> getClima() {
        Map<String, Object> clima = Map.of(
                "temperatura", 25,
                "descripcion", "Soleado con nubes dispersas",
                "humedad", 60,
                "icono", "☀️"
        );
        return ResponseEntity.ok(clima);
    }
}
