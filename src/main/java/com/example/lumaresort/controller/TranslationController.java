package com.example.lumaresort.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/translate")
public class TranslationController {

    private static final Logger logger = LoggerFactory.getLogger(TranslationController.class);
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String MYMEMORY_API = "https://api.mymemory.translated.net/get";

    @GetMapping
    public ResponseEntity<?> translate(
            @RequestParam String q,
            @RequestParam String langpair,
            @RequestParam(required = false) String de) {

        try {
            logger.info("Traduciendo: '{}' | langpair: '{}'", q, langpair);

            String email = de != null ? de : "info@lumaresort.com";

            // Construir URL usando UriComponentsBuilder (maneja la codificación automáticamente)
            String url = UriComponentsBuilder
                    .fromHttpUrl(MYMEMORY_API)
                    .queryParam("q", q)
                    .queryParam("langpair", langpair)
                    .queryParam("de", email)
                    .encode() // Codifica solo una vez
                    .toUriString();

            logger.info("URL final: {}", url);

            // Configurar headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "LumaResort/1.0");
            headers.set("Accept", "application/json");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            // Hacer petición a MyMemory
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            logger.info("Traducción exitosa. Status: {}", response.getStatusCode());

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response.getBody());

        } catch (RestClientException e) {
            logger.error("Error al llamar a MyMemory API: {}", e.getMessage());
            return createErrorResponse("Error al comunicarse con el servicio de traducción: " + e.getMessage());

        } catch (Exception e) {
            logger.error("Error inesperado en traducción: {}", e.getMessage(), e);
            return createErrorResponse("Error inesperado: " + e.getMessage());
        }
    }

    private ResponseEntity<Map<String, Object>> createErrorResponse(String message) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", message);
        errorResponse.put("responseData", Map.of("translatedText", ""));

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorResponse);
    }
}
