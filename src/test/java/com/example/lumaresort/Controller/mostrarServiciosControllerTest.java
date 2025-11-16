package com.example.lumaresort.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import com.example.lumaresort.entities.Servicio;
import com.example.lumaresort.service.ServicioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.lumaresort.controller.mostrarServiciosController;

@WebMvcTest(controllers = mostrarServiciosController.class)
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class mostrarServiciosControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServicioService servicioService;

    @Autowired
    private ObjectMapper objectMapper;

    //GET 
    @Test
    public void testListarServicios_RetornarListaDeServicios() throws Exception {
        // Arrange
        when(servicioService.findAll()).thenReturn(
            List.of(
                createServicio(1L, "Spa", "Servicio de spa y masajes", 50.0f),
                createServicio(2L, "Gimnasio", "Acceso al gimnasio", 30.0f)
            )
        );

        // Act
        ResultActions response = mockMvc.perform(get("/servicios"));

        // Assert
        response.andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.size()").value(2));
    }

    // GET by ID
    @Test
    public void testObtenerServicio_CuandoExiste_RetornarServicio() throws Exception {
        // Arrange
        when(servicioService.findById(1L)).thenReturn(
            Optional.of(
                createServicio(1L, "Spa", "Servicio de spa y masajes", 50.0f)
            )
        );

        // Act
        ResultActions response = mockMvc.perform(get("/servicios/1"));

        // Assert
        response.andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.idServicio").value(1))
                .andExpect(jsonPath("$.nombre").value("Spa"))
                .andExpect(jsonPath("$.descripcion").value("Servicio de spa y masajes"))
                .andExpect(jsonPath("$.precio").value(50.0));
    }

    //POST
    @Test
    public void testCrearServicio_CrearYRetornarServicio() throws Exception {
        // Arrange
        Servicio nuevoServicio = createServicio(null, "Piscina", "Acceso a la piscina", 20.0f);
        Servicio servicioGuardado = createServicio(3L, "Piscina", "Acceso a la piscina", 20.0f);

        when(servicioService.save(any(Servicio.class))).thenReturn(servicioGuardado);

        // Act
        ResultActions response = mockMvc.perform(post("/servicios")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(nuevoServicio)));

        // Assert
        response.andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.idServicio").value(3))
                .andExpect(jsonPath("$.nombre").value("Piscina"))
                .andExpect(jsonPath("$.precio").value(20.0));
    }

    //PUT
    @Test
    public void testActualizarServicio_CuandoExiste_ActualizarYRetornar() throws Exception {
        // Arrange
        Servicio servicioActualizado = createServicio(1L, "Spa Premium", 
            "Servicio de spa premium con masajes", 80.0f);

        when(servicioService.update(eq(1L), any(Servicio.class))).thenReturn(servicioActualizado);

        // Act
        ResultActions response = mockMvc.perform(put("/servicios/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(servicioActualizado)));

        // Assert
        response.andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.idServicio").value(1))
                .andExpect(jsonPath("$.nombre").value("Spa Premium"))
                .andExpect(jsonPath("$.precio").value(80.0));
    }

    //DELETE
    @Test
    public void testEliminarServicio_CuandoExiste_Eliminar() throws Exception {
        // Arrange
        doNothing().when(servicioService).delete(1L);

        // Act
        ResultActions response = mockMvc.perform(delete("/servicios/1"));

        // Assert
        response.andExpect(status().isOk());
    }

    // crear servicios de prueba
    private Servicio createServicio(Long id, String nombre, String descripcion, Float precio) {
        Servicio servicio = new Servicio();
        servicio.setIdServicio(id);
        servicio.setNombre(nombre);
        servicio.setDescripcion(descripcion);
        servicio.setPrecio(precio);
        return servicio;
    }
}