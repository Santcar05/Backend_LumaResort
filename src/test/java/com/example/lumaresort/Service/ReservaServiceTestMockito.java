package com.example.lumaresort.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.example.lumaresort.entities.Habitacion;
import com.example.lumaresort.entities.Reserva;
import com.example.lumaresort.entities.Servicio;
import com.example.lumaresort.entities.Usuario;
import com.example.lumaresort.repository.HabitacionRepository;
import com.example.lumaresort.repository.ReservaRepository;
import com.example.lumaresort.repository.ServicioRepository;
import com.example.lumaresort.repository.UsuarioRepository;
import com.example.lumaresort.service.ReservaService;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class ReservaServiceTestMockito {

    //Servicio a probar
    @InjectMocks
    private ReservaService reservaService;

    @Mock
    private HabitacionRepository habitacionRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private ServicioRepository servicioRepository;

    // PRUEBA 1 - TU PRUEBA ORIGINAL (MANTENIDA)
    @Test
    public void testSave() {
        //Arrange
        // Crear usuario y habitación persistidos
        Usuario usuario = new Usuario();
        Habitacion habitacion = new Habitacion();

        usuarioRepository.save(usuario);
        habitacionRepository.save(habitacion);

        Reserva reserva1 = new Reserva(new Date(2023, 10, 1), new Date(2023, 10, 5), 2, "CONFIRMADA", usuario, habitacion);
        //Stub

        when(reservaRepository.save(reserva1)).thenReturn(reserva1);
        //Act

        reservaService.save(reserva1);
        //Assert
        assertNotNull(reserva1);
        assertEquals("CONFIRMADA", reserva1.getEstado());
    }

    // PRUEBA 2 - save_Reserva_DeberiaCambiarEstadoHabitacionAOcupada
    @Test
    void save_Reserva_DeberiaCambiarEstadoHabitacionAOcupada() {
        // Arrange
        Habitacion habitacion = Habitacion.builder()
            .idHabitacion(1L)
            .estado("Disponible")
            .build();
        
        Reserva reserva = Reserva.builder()
            .habitacion(habitacion)
            .estado("Confirmada")
            .build();
        
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reserva);

        // Act
        Reserva resultado = reservaService.save(reserva);

        // Assert
        assertEquals("Ocupada", habitacion.getEstado());
        verify(reservaRepository, times(1)).save(reserva);
    }

    // PRUEBA 3 - contratarServicio_ReservaExistente_DeberiaAgregarServicio
    @Test
    void contratarServicio_ReservaExistente_DeberiaAgregarServicio() {
        // Arrange
        Long idServicio = 1L;
        Long idHabitacion = 1L;
        
        Reserva reserva = Reserva.builder()
            .idReserva(1L)
            .servicios(new ArrayList<>())
            .build();
        
        Servicio servicio = new Servicio();
        servicio.setIdServicio(idServicio);
        
        when(reservaRepository.findByHabitacionIdHabitacion(idHabitacion))
            .thenReturn(List.of(reserva));
        when(servicioRepository.findById(idServicio))
            .thenReturn(Optional.of(servicio));
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reserva);

        // Act
        Reserva resultado = reservaService.contratarServicio(idServicio, idHabitacion);

        // Assert
        assertTrue(resultado.getServicios().contains(servicio));
        verify(reservaRepository, times(1)).save(reserva);
    }

    // PRUEBA 4 - contratarServicio_ReservaNoExistente_DeberiaLanzarExcepcion
    @Test
    void contratarServicio_ReservaNoExistente_DeberiaLanzarExcepcion() {
        // Arrange
        Long idServicio = 1L;
        Long idHabitacion = 999L; // No existe
        
        when(reservaRepository.findByHabitacionIdHabitacion(idHabitacion))
            .thenReturn(List.of()); // Lista vacía

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reservaService.contratarServicio(idServicio, idHabitacion);
        });
        
        assertEquals("No se encontró la reserva con idHabitacion de ID: " + idHabitacion, exception.getMessage());
    }

    // PRUEBA 5 - removerServicio_ReservaExistente_DeberiaEliminarServicio
    @Test
    void removerServicio_ReservaExistente_DeberiaEliminarServicio() {
        // Arrange
        Long idReserva = 1L;
        Long idServicio = 1L;
        
        Servicio servicio = new Servicio();
        servicio.setIdServicio(idServicio);
        
        Reserva reserva = Reserva.builder()
            .idReserva(idReserva)
            .servicios(new ArrayList<>(List.of(servicio)))
            .build();
        
        when(reservaRepository.findById(idReserva)).thenReturn(Optional.of(reserva));
        when(reservaRepository.saveAndFlush(any(Reserva.class))).thenReturn(reserva);

        // Act
        reservaService.removerServicio(idReserva, idServicio);

        // Assert
        assertFalse(reserva.getServicios().contains(servicio));
        verify(reservaRepository, times(1)).saveAndFlush(reserva);
    }

    // PRUEBA 6 - obtenerReservasDisponiblesParaServicio_DeberiaFiltrarCorrectamente
    @Test
    void obtenerReservasDisponiblesParaServicio_DeberiaFiltrarCorrectamente() {
        // Arrange
        Long idServicio = 1L;
        
        Reserva reservaConServicio = Reserva.builder()
            .idReserva(1L)
            .estado("Confirmada")
            .servicios(List.of(new Servicio())) // Tiene servicios
            .build();
        
        Reserva reservaSinServicio = Reserva.builder()
            .idReserva(2L)
            .estado("Confirmada")
            .servicios(new ArrayList<>()) // No tiene servicios
            .build();
        
        Reserva reservaCancelada = Reserva.builder()
            .idReserva(3L)
            .estado("Cancelada")
            .servicios(new ArrayList<>())
            .build();
        
        when(reservaRepository.findByServiciosIdServicio(idServicio))
            .thenReturn(List.of(reservaConServicio));
        when(reservaRepository.findAll())
            .thenReturn(List.of(reservaConServicio, reservaSinServicio, reservaCancelada));

        // Act
        List<Reserva> resultado = reservaService.obtenerReservasDisponiblesParaServicio(idServicio);

        // Assert
        assertEquals(1, resultado.size());
        assertEquals(2L, resultado.get(0).getIdReserva());
        verify(reservaRepository, times(1)).findByServiciosIdServicio(idServicio);
        verify(reservaRepository, times(1)).findAll();
    }

    // PRUEBA 7 - findByUsuarioId_DeberiaRetornarReservasUsuario
    @Test
    void findByUsuarioId_DeberiaRetornarReservasUsuario() {
        // Arrange
        Long usuarioId = 1L;
        List<Reserva> reservasEsperadas = List.of(
            Reserva.builder().idReserva(1L).build(),
            Reserva.builder().idReserva(2L).build()
        );
        
        when(reservaRepository.findByUsuarioIdUsuario(usuarioId))
            .thenReturn(reservasEsperadas);

        // Act
        List<Reserva> resultado = reservaService.findByUsuarioId(usuarioId);

        // Assert
        assertEquals(2, resultado.size());
        verify(reservaRepository, times(1)).findByUsuarioIdUsuario(usuarioId);
    }

    // PRUEBA 8 - findById_ReservaExistente_DeberiaRetornarReserva
    @Test
    void findById_ReservaExistente_DeberiaRetornarReserva() {
        // Arrange
        Long idReserva = 1L;
        Reserva reservaEsperada = Reserva.builder().idReserva(idReserva).build();
        
        when(reservaRepository.findById(idReserva))
            .thenReturn(Optional.of(reservaEsperada));

        // Act
        Reserva resultado = reservaService.findById(idReserva);

        // Assert
        assertNotNull(resultado);
        assertEquals(idReserva, resultado.getIdReserva());
        verify(reservaRepository, times(1)).findById(idReserva);
    }

    // PRUEBA 9 - obtenerHabitacionesDisponiblesParaServicio_DeberiaFiltrarHabitaciones
    @Test
    void obtenerHabitacionesDisponiblesParaServicio_DeberiaFiltrarHabitaciones() {
        // Arrange
        Long idServicio = 1L;
        
        Habitacion habitacion1 = Habitacion.builder().idHabitacion(1L).build();
        Habitacion habitacion2 = Habitacion.builder().idHabitacion(2L).build();
        
        Reserva reservaConServicio = Reserva.builder()
            .idReserva(1L)
            .habitacion(habitacion1)
            .build();
        
        Reserva reservaSinServicio = Reserva.builder()
            .idReserva(2L)
            .habitacion(habitacion2)
            .build();
        
        when(reservaRepository.findByServiciosIdServicio(idServicio))
            .thenReturn(List.of(reservaConServicio));
        when(reservaRepository.findAll())
            .thenReturn(List.of(reservaConServicio, reservaSinServicio));

        // Act
        List<Habitacion> resultado = reservaService.obtenerHabitacionesDisponiblesParaServicio(idServicio);

        // Assert
        assertEquals(1, resultado.size());
        assertEquals(2L, resultado.get(0).getIdHabitacion());
        verify(reservaRepository, times(1)).findByServiciosIdServicio(idServicio);
        verify(reservaRepository, times(1)).findAll();
    }
}