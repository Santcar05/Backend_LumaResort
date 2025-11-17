package com.example.lumaresort.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String MYMEMORY_API = "https://api.mymemory.translated.net/get";

    @GetMapping
    public ResponseEntity<String> translate(
            @RequestParam String q,
            @RequestParam String langpair,
            @RequestParam(required = false) String de) {

        try {
            logger.info("Traduciendo: '{}' | langpair: '{}'", q, langpair);

            String email = de != null ? de : "info@lumaresort.com";

            String url = UriComponentsBuilder
                    .fromHttpUrl(MYMEMORY_API)
                    .queryParam("q", q)
                    .queryParam("langpair", langpair)
                    .queryParam("de", email)
                    .encode()
                    .toUriString();

            logger.info("URL final: {}", url);

            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "LumaResort/1.0");
            headers.set("Accept", "application/json");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            String body = response.getBody();

            // Validar que la respuesta sea JSON válido
            if (body != null && body.startsWith("INVALID")) {
                // MyMemory devolvió error en texto plano, convertir a JSON
                logger.error("Error de MyMemory: {}", body);
                return createErrorResponse(body);
            }

            logger.info("Traducción exitosa. Status: {}", response.getStatusCode());

            // Devolver el JSON directamente como lo devuelve MyMemory
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body);

        } catch (RestClientException e) {
            logger.error("Error al llamar a MyMemory API: {}", e.getMessage());
            return createErrorResponse("Error al comunicarse con el servicio de traducción: " + e.getMessage());

        } catch (Exception e) {
            logger.error("Error inesperado en traducción: {}", e.getMessage(), e);
            return createErrorResponse("Error inesperado: " + e.getMessage());
        }
    }

    private ResponseEntity<String> createErrorResponse(String message) {
        try {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", message);

            Map<String, String> responseData = new HashMap<>();
            responseData.put("translatedText", "");
            errorResponse.put("responseData", responseData);

            // Convertir Map a JSON String
            String jsonResponse = objectMapper.writeValueAsString(errorResponse);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(jsonResponse);

        } catch (Exception e) {
            logger.error("Error al crear respuesta de error: {}", e.getMessage());
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"error\":\"" + message + "\",\"responseData\":{\"translatedText\":\"\"}}");
        }
    }
}
