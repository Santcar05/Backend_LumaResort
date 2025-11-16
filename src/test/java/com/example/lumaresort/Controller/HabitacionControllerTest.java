package com.example.lumaresort.Controller;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.example.lumaresort.controller.HabitacionController;
import com.example.lumaresort.entities.Habitacion;
import com.example.lumaresort.entities.TipoHabitacion;
import com.example.lumaresort.service.HabitacionService;
import com.example.lumaresort.service.TipoHabitacionService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.*;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = HabitacionController.class)
@ActiveProfiles("test")
@RunWith(org.springframework.test.context.junit4.SpringRunner.class)
public class HabitacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HabitacionService habitacionService;

    @MockBean
    private TipoHabitacionService tipoHabitacionService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void crearHabitacion_test() throws Exception {
        //Arrange
        TipoHabitacion tipo1 = TipoHabitacion.builder()
                .id(1L)
                .nombre("Individual")
                .descripcion("Habitación para una persona")
                .imagenes(java.util.Arrays.asList("https://example.com/imagen_individual.jpg"))
                .caracteristicas(java.util.Arrays.asList("Cama individual, Baño privado, Wi-Fi gratuito"))
                .precio(50.0)
                .build();

        when(tipoHabitacionService.crear(Mockito.any(TipoHabitacion.class))).thenReturn(tipo1);

        Habitacion habitacion1 = Habitacion.builder()
                .numero("101")
                .precioPorNoche(100.0f)
                .estado("Ocupada")
                .capacidad(1)
                .descripcion("Habitación individual cómoda")
                .tipoHabitacion(tipo1)
                .build();

        when(habitacionService.crearHabitacion(Mockito.any(Habitacion.class), Mockito.any(Long.class))).thenReturn(habitacion1);
        ResultActions response = mockMvc.perform(
                post("/habitaciones")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(habitacion1))
        //.contentType("application/json")
        //.content(objectMapper.writeValueAsString(habitacion1))
        );

        /*
             @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHabitacion;

    private String numero;
    private float precioPorNoche;
    private String estado;
    private Integer capacidad;
    private String descripcion;

    // Muchas habitaciones pueden ser de un tipo
    @ManyToOne
    @JoinColumn(name = "idTipoHabitacion") // FK hacia TipoHabitacion
    private TipoHabitacion tipoHabitacion;
         */
        response.andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.numero").value(habitacion1.getNumero()))
                .andExpect(jsonPath("$.precioPorNoche").value(habitacion1.getPrecioPorNoche()))
                .andExpect(jsonPath("$.estado").value(habitacion1.getEstado()))
                .andExpect(jsonPath("$.capacidad").value(habitacion1.getCapacidad()))
                .andExpect(jsonPath("$.descripcion").value(habitacion1.getDescripcion()))
                .andExpect(jsonPath("$.tipoHabitacion.id").value(habitacion1.getTipoHabitacion().getId()));
    }

    @Test
    public void obtenerHabitacion_Test() throws Exception {
        // Arrange
        TipoHabitacion tipo1 = TipoHabitacion.builder()
                .id(1L)
                .nombre("Individual")
                .descripcion("Habitación para una persona")
                .imagenes(java.util.Arrays.asList("https://example.com/imagen_individual.jpg"))
                .caracteristicas(java.util.Arrays.asList("Cama individual, Baño privado, Wi-Fi gratuito"))
                .precio(50.0)
                .build();

        Habitacion habitacion1 = Habitacion.builder()
                .idHabitacion(1L)
                .numero("101")
                .precioPorNoche(100.0f)
                .estado("Ocupada")
                .capacidad(1)
                .descripcion("Habitación individual cómoda")
                .tipoHabitacion(tipo1)
                .build();

        when(habitacionService.buscarHabitacionPorId(1L)).thenReturn(habitacion1);

        // Act
        ResultActions response = mockMvc.perform(
                get("/habitaciones/1")
                        .contentType("application/json")
        );

        // Assert
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.idHabitacion").value(habitacion1.getIdHabitacion()))
                .andExpect(jsonPath("$.numero").value("101"))
                .andExpect(jsonPath("$.precioPorNoche").value(100.0f))
                .andExpect(jsonPath("$.estado").value("Ocupada"))
                .andExpect(jsonPath("$.capacidad").value(1))
                .andExpect(jsonPath("$.descripcion").value("Habitación individual cómoda"))
                .andExpect(jsonPath("$.tipoHabitacion.id").value(1L));
    }

}
