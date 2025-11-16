package com.example.lumaresort.service;

import org.springframework.stereotype.Service;

import com.example.lumaresort.entities.Mensaje;
import com.example.lumaresort.repository.HistorialRepository;

@Service
public class ChatbotService {

    private final OpenRouterService openRouterService;
    private final HistorialRepository historialRepository;

    public ChatbotService(OpenRouterService openRouterService, HistorialRepository historialRepository) {
        this.openRouterService = openRouterService;
        this.historialRepository = historialRepository;
    }

    /**
     * Procesa un mensaje de texto normal
     */
    public String procesarMensaje(String mensajeUsuario) {
        // Guardar mensaje del usuario
        historialRepository.save(new Mensaje(mensajeUsuario, true));

        // Obtener respuesta de IA
        String respuestaIA = openRouterService.generarRespuesta(mensajeUsuario);

        // Guardar respuesta del bot
        historialRepository.save(new Mensaje(respuestaIA, false));

        return respuestaIA;
    }

    /**
     * Procesa una imagen con IA Vision
     */
    public String procesarImagenConIA(String mensaje, String imageDataUrl) {
        // Guardar mensaje del usuario indicando que envió imagen
        historialRepository.save(new Mensaje("[Usuario envió una imagen]", true));

        // Enviar imagen a OpenRouter para análisis
        String respuestaIA = openRouterService.generarRespuestaConImagen(mensaje, imageDataUrl);

        // Guardar respuesta del bot
        historialRepository.save(new Mensaje(respuestaIA, false));

        return respuestaIA;
    }

    /**
     * Procesa un mensaje con contexto adicional (para audio)
     */
    public String procesarMensajeConContexto(String mensajeUsuario, String contexto) {
        historialRepository.save(new Mensaje(mensajeUsuario + " [" + contexto + "]", true));

        String respuestaIA = openRouterService.generarRespuesta(mensajeUsuario);

        historialRepository.save(new Mensaje(respuestaIA, false));

        return respuestaIA;
    }
}