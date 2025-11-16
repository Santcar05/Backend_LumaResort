package com.example.lumaresort.controller;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.Base64;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.lumaresort.service.ChatbotService;

@RestController
@RequestMapping("/api/chatbot")
@CrossOrigin(origins = "http://localhost:4200")
public class ChatbotController {

    private final ChatbotService chatbotService;
    private static final String UPLOAD_DIR = "uploads/";

    public ChatbotController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    /**
     * Endpoint principal para mensajes de texto
     */
    @PostMapping
    public ResponseEntity<Map<String, String>> enviarMensaje(@RequestBody Map<String, String> body) {
        String mensaje = body.get("mensaje");
        if (mensaje == null || mensaje.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("respuesta", "Por favor ingresa un mensaje."));
        }

        String respuesta = chatbotService.procesarMensaje(mensaje);
        return ResponseEntity.ok(Map.of("respuesta", respuesta));
    }

    /**
     * Endpoint para subir y analizar imágenes con IA
     */
    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            // Validar que sea una imagen
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "El archivo debe ser una imagen (JPG, PNG, GIF, WEBP)"));
            }

            // Validar tamaño (5MB máximo)
            if (file.getSize() > 5 * 1024 * 1024) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "La imagen no debe superar 5MB"));
            }

            // Crear directorio si no existe
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generar nombre único
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : ".jpg";
            String fileName = System.currentTimeMillis() + extension;
            Path filePath = uploadPath.resolve(fileName);

            // Guardar archivo localmente
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Convertir imagen a base64 para enviar a OpenRouter
            byte[] imageBytes = file.getBytes();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            // Determinar el tipo MIME correcto
            String mimeType = contentType;

            // Crear data URL completa
            String imageDataUrl = "data:" + mimeType + ";base64," + base64Image;

            // Procesar la imagen con IA (enviar base64)
            String respuestaIA = chatbotService.procesarImagenConIA(
                    "El usuario ha enviado una imagen. Analiza la imagen y proporciona información útil relacionada con servicios de hotel, habitaciones, turismo o lo que sea relevante según el contenido de la imagen.",
                    imageDataUrl
            );

            Map<String, String> response = new HashMap<>();
            response.put("respuesta", respuestaIA);
            response.put("fileUrl", "/uploads/" + fileName);

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al procesar la imagen: " + e.getMessage()));
        }
    }

    /**
     * Endpoint para procesar audio
     */
    @PostMapping("/audio")
    public ResponseEntity<Map<String, String>> processAudio(@RequestParam("audio") MultipartFile audio) {
        try {
            // Crear directorio si no existe
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Guardar audio
            String fileName = System.currentTimeMillis() + "_audio.webm";
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(audio.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Aquí iría la integración con Speech-to-Text
            // Por ahora, simulamos una transcripción
            String transcripcionSimulada = "Quiero hacer una reserva para dos personas";

            // Procesar el mensaje transcrito con la IA
            String respuestaIA = chatbotService.procesarMensaje(transcripcionSimulada);

            Map<String, String> response = new HashMap<>();
            response.put("transcripcion", transcripcionSimulada);
            response.put("respuesta", respuestaIA);
            response.put("audioUrl", "/uploads/" + fileName);

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al procesar el audio: " + e.getMessage()));
        }
    }

    /**
     * Endpoint para servir archivos subidos
     */
    @GetMapping("/uploads/{filename:.+}")
    public ResponseEntity<byte[]> getFile(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(UPLOAD_DIR).resolve(filename);
            byte[] fileContent = Files.readAllBytes(filePath);

            // Determinar tipo de contenido
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(fileContent);

        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
