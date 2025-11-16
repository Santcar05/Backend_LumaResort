package com.example.lumaresort.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/translate")
@CrossOrigin(origins = "http://localhost:4200")
public class TranslationController {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String MYMEMORY_API = "https://api.mymemory.translated.net/get";

    @GetMapping
    public ResponseEntity<?> translate(
            @RequestParam String q,
            @RequestParam String langpair,
            @RequestParam(required = false) String de) {

        try {
            // Construir URL con parámetros
            URI uri = UriComponentsBuilder
                    .fromHttpUrl(MYMEMORY_API)
                    .queryParam("q", q)
                    .queryParam("langpair", langpair)
                    .queryParam("de", de != null ? de : "santcar05@lumaresort.com")
                    .build()
                    .toUri();

            // Hacer petición a MyMemory
            ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

            return ResponseEntity.ok(response.getBody());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
