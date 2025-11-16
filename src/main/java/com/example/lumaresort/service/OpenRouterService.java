package com.example.lumaresort.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.lumaresort.entities.*;
import com.example.lumaresort.repository.HistorialRepository;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.annotation.PostConstruct;

@Service
public class OpenRouterService {

    private WebClient webClient;

    @Value("${openrouter.api.url:https://openrouter.ai/api/v1}")
    private String apiUrl;

    @Value("${openrouter.api.key}")
    private String apiKey;

    @Value("${openrouter.model}")
    private String model;

    // Modelo con capacidad de visión
    @Value("${openrouter.vision.model:google/gemini-2.0-flash-exp:free}")
    private String visionModel;

    private final HistorialRepository historialRepository;

    @Autowired
    ServicioService servicioService;

    @Autowired
    TipoHabitacionService tipoHabitacionService;

    @Autowired
    HabitacionService habitacionService;

    public OpenRouterService(HistorialRepository historialRepository) {
        this.historialRepository = historialRepository;
    }

    @PostConstruct
    public void init() {
        this.webClient = WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("HTTP-Referer", "http://localhost:8080")
                .defaultHeader("X-Title", "LumaResort Assistant")
                .build();

        System.out.println("✅ WebClient inicializado correctamente");
        System.out.println("📷 Modelo de visión: " + visionModel);
    }

    /**
     * Genera respuesta con análisis de imagen
     */
    public String generarRespuestaConImagen(String mensajeUsuario, String imageDataUrl) {
        try {
            List<Mensaje> historial = historialRepository.findAll();
            List<TipoHabitacion> tipos = tipoHabitacionService.findAll();
            List<Servicio> servicios = servicioService.findAll();
            List<Habitacion> totalHabitaciones = habitacionService.listarTodos();
            List<Habitacion> habitacionesDisponibles = totalHabitaciones.stream()
                    .filter(h -> "DISPONIBLE".equals(h.getEstado()))
                    .toList();

            List<Map<String, Object>> messages = new ArrayList<>();

            // Prompt del sistema (mismo que antes)
            messages.add(Map.of("role", "system", "content",
                    "# IDENTIDAD Y PROPÓSITO\n"
                    + "Eres LUMA, la asistente virtual oficial de LumaResort, un hotel de lujo excepcional.\n\n"
                    + "## INFORMACIÓN DEL RESORT\n"
                    + "Tipos de habitación: " + tipos + "\n"
                    + "Servicios: " + servicios + "\n"
                    + "Habitaciones disponibles: " + habitacionesDisponibles.size() + "/" + totalHabitaciones.size() + "\n\n"
                    + "Cuando analices imágenes:\n"
                    + "  • Describe lo que ves de forma clara y profesional\n"
                    + "  • Relaciona el contenido con servicios o habitaciones del hotel si es relevante\n"
                    + "  • Sé útil y proactiva en tus sugerencias\n"
                    + "  • No uses formato Markdown (**, __, ##) - solo MAYÚSCULAS para énfasis\n"
            ));

            // Agregar historial previo (sin imágenes)
            for (Mensaje msg : historial.subList(Math.max(0, historial.size() - 10), historial.size())) {
                if (!msg.getContenido().contains("[Usuario envió una imagen]")) {
                    messages.add(Map.of(
                            "role", msg.isEsUsuario() ? "user" : "assistant",
                            "content", msg.getContenido()
                    ));
                }
            }

            // Mensaje del usuario con imagen
            List<Map<String, Object>> contentArray = new ArrayList<>();
            contentArray.add(Map.of("type", "text", "text", mensajeUsuario));
            contentArray.add(Map.of(
                    "type", "image_url",
                    "image_url", Map.of("url", imageDataUrl)
            ));

            messages.add(Map.of(
                    "role", "user",
                    "content", contentArray
            ));

            Map<String, Object> request = Map.of(
                    "model", visionModel, // Usar modelo con visión
                    "messages", messages
            );

            System.out.println("📤 Enviando imagen a OpenRouter con modelo: " + visionModel);

            JsonNode response = webClient.post()
                    .uri("/chat/completions")
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            System.out.println("📩 Respuesta del modelo con imagen recibida");

            JsonNode choices = response.path("choices");
            if (choices.isArray() && !choices.isEmpty()) {
                return choices.get(0).path("message").path("content").asText();
            }

            return "Lo siento, no pude analizar la imagen correctamente.";

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Error al analizar imagen: " + e.getMessage());
            return "Hubo un error al analizar tu imagen. Por favor, intenta nuevamente.";
        }
    }

    /**
     * Genera respuesta de texto normal (sin imagen)
     */
    public String generarRespuesta(String mensajeUsuario) {
        try {
            List<Mensaje> historial = historialRepository.findAll();
            List<TipoHabitacion> tipos = tipoHabitacionService.findAll();
            List<Servicio> servicios = servicioService.findAll();
            List<Habitacion> totalHabitaciones = habitacionService.listarTodos();
            List<Habitacion> habitacionesDisponibles = totalHabitaciones.stream()
                    .filter(h -> "DISPONIBLE".equals(h.getEstado()))
                    .toList();

            List<Map<String, String>> messages = new ArrayList<>();

            // Prompt del sistema (tu prompt actual completo)
            messages.add(Map.of("role", "system", "content",
                    "# IDENTIDAD Y PROPÓSITO\n"
                    + "Eres LUMA, la asistente virtual oficial de LumaResort, un hotel de lujo excepcional. "
                    + "Tu misión es brindar atención personalizada, cálida y profesional a nuestros huéspedes y visitantes, "
                    + "ayudándolos con reservas, información sobre servicios y cualquier consulta relacionada con su estadía.\n\n"
                    + "# INFORMACIÓN DEL RESORT\n\n"
                    + "## TIPOS DE HABITACIÓN DISPONIBLES:" + tipos.toString() + "\n\n"
                    + "## SERVICIOS EXCLUSIVOS:" + servicios.toString() + "\n\n"
                    + "## DISPONIBILIDAD ACTUAL:\n"
                    + "  • Total de habitaciones: " + totalHabitaciones.size() + "\n"
                    + "  • Habitaciones disponibles: " + habitacionesDisponibles.size() + "\n\n"
            // ... (resto de tu prompt completo)
            ));

            // Agregar historial
            for (Mensaje msg : historial) {
                messages.add(Map.of(
                        "role", msg.isEsUsuario() ? "user" : "assistant",
                        "content", msg.getContenido()
                ));
            }

            // Mensaje actual
            messages.add(Map.of("role", "user", "content", mensajeUsuario));

            Map<String, Object> request = Map.of(
                    "model", model,
                    "messages", messages
            );

            JsonNode response = webClient.post()
                    .uri("/chat/completions")
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            JsonNode choices = response.path("choices");
            if (choices.isArray() && !choices.isEmpty()) {
                return choices.get(0).path("message").path("content").asText();
            }

            return "Lo siento, no recibí una respuesta válida del modelo.";

        } catch (Exception e) {
            e.printStackTrace();
            return "Hubo un error al conectar con el modelo de IA: " + e.getMessage();
        }
    }
}
