package com.example.lumaresort.Service;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
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

import jakarta.transaction.Transactional;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class ReservaServiceTestUnitario {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private HabitacionRepository habitacionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ServicioRepository servicioRepository;

    private Usuario usuarioBase;
    private Habitacion habitacionBase;

    /**
     * Método que se ejecuta antes de cada prueba para crear datos base
     */
    @BeforeEach
    void setup() {
        usuarioRepository.deleteAll();
        habitacionRepository.deleteAll();
        reservaRepository.deleteAll();
        servicioRepository.deleteAll();

        usuarioBase = usuarioRepository.save(new Usuario());
        habitacionBase = habitacionRepository.save(new Habitacion());
        habitacionBase.setEstado("Disponible");
        habitacionRepository.save(habitacionBase);
    }

    // ========================
    // PRUEBAS DE INTEGRACIÓN BASE
    // ========================
    @Test
    public void testFindAll() {
        Reserva reserva1 = new Reserva(new Date(2023, 10, 1), new Date(2023, 10, 5), 2, "CONFIRMADA", usuarioBase, habitacionBase);
        Reserva reserva2 = new Reserva(new Date(2023, 10, 6), new Date(2023, 10, 10), 2, "CONFIRMADA", usuarioBase, habitacionBase);
        Reserva reserva3 = new Reserva(new Date(2023, 10, 11), new Date(2023, 10, 15), 2, "CONFIRMADA", usuarioBase, habitacionBase);

        reservaService.save(reserva1);
        reservaService.save(reserva2);
        reservaService.save(reserva3);

        List<Reserva> reservas = reservaService.findAll();
        Assertions.assertEquals(3, reservas.size());
    }

    @Test
    public void testFindById() {
        Reserva reserva = reservaService.save(
                new Reserva(new Date(2023, 10, 1), new Date(2023, 10, 5), 2, "CONFIRMADA", usuarioBase, habitacionBase)
        );

        Reserva encontrada = reservaService.findById(reserva.getIdReserva());
        Assertions.assertNotNull(encontrada);
        Assertions.assertEquals(reserva.getIdReserva(), encontrada.getIdReserva());
    }

    @Test
    public void testSave() {
        Reserva reserva = reservaService.save(
                new Reserva(new Date(2023, 10, 1), new Date(2023, 10, 5), 2, "CONFIRMADA", usuarioBase, habitacionBase)
        );

        Assertions.assertNotNull(reserva.getIdReserva());
        Assertions.assertEquals("CONFIRMADA", reserva.getEstado());
    }

    // ========================
    // PRUEBAS ADAPTADAS DE MOCKITO
    // ========================
    @Test
    @Transactional
    public void save_Reserva_DeberiaCambiarEstadoHabitacionAOcupada() {
        Reserva reserva = new Reserva(new Date(), new Date(), 2, "Confirmada", usuarioBase, habitacionBase);
        reservaService.save(reserva);

        Habitacion habitacionActualizada = habitacionRepository.findById(habitacionBase.getIdHabitacion()).orElseThrow();
        Assertions.assertEquals("Ocupada", habitacionActualizada.getEstado());
    }

    @Test
    @Transactional
    public void contratarServicio_ReservaExistente_DeberiaAgregarServicio() {
        Reserva reserva = reservaRepository.save(
                new Reserva(new Date(), new Date(), 2, "Confirmada", usuarioBase, habitacionBase)
        );

        Servicio servicio = servicioRepository.save(new Servicio());
        Reserva actualizada = reservaService.contratarServicio(servicio.getIdServicio(), habitacionBase.getIdHabitacion());

        Assertions.assertTrue(actualizada.getServicios().contains(servicio));
    }

    @Test
    public void contratarServicio_ReservaNoExistente_DeberiaLanzarExcepcion() {
        Servicio servicio = servicioRepository.save(new Servicio());
        Long idHabitacionInexistente = 999L;

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            reservaService.contratarServicio(servicio.getIdServicio(), idHabitacionInexistente);
        });

        Assertions.assertEquals("No se encontró la reserva con idHabitacion de ID: " + idHabitacionInexistente, exception.getMessage());
    }

    @Test
    @Transactional
    public void removerServicio_ReservaExistente_DeberiaEliminarServicio() {
        Servicio servicio = servicioRepository.save(new Servicio());
        Reserva reserva = reservaRepository.save(new Reserva(new Date(), new Date(), 2, "Confirmada", usuarioBase, habitacionBase));
        reserva.getServicios().add(servicio);
        reservaRepository.saveAndFlush(reserva);

        reservaService.removerServicio(reserva.getIdReserva(), servicio.getIdServicio());

        Reserva actualizada = reservaRepository.findById(reserva.getIdReserva()).orElseThrow();

        Assertions.assertFalse(actualizada.getServicios().contains(servicio));
    }

    @Test
    public void findByUsuarioId_DeberiaRetornarReservasUsuario() {
        reservaRepository.save(new Reserva(new Date(), new Date(), 2, "Confirmada", usuarioBase, habitacionBase));
        reservaRepository.save(new Reserva(new Date(), new Date(), 2, "Confirmada", usuarioBase, habitacionBase));

        List<Reserva> reservas = reservaService.findByUsuarioId(usuarioBase.getIdUsuario());
        Assertions.assertEquals(2, reservas.size());
    }

    @Test
    public void obtenerReservasDisponiblesParaServicio_DeberiaFiltrarCorrectamente() {
        Servicio servicio = servicioRepository.save(new Servicio());
        Reserva reservaConServicio = new Reserva(new Date(), new Date(), 2, "Confirmada", usuarioBase, habitacionBase);
        reservaConServicio.getServicios().add(servicio);
        reservaRepository.save(reservaConServicio);

        Reserva reservaSinServicio = reservaRepository.save(
                new Reserva(new Date(), new Date(), 2, "Confirmada", usuarioBase, habitacionBase)
        );

        List<Reserva> resultado = reservaService.obtenerReservasDisponiblesParaServicio(servicio.getIdServicio());
        Assertions.assertEquals(1, resultado.size());
        Assertions.assertEquals(reservaSinServicio.getIdReserva(), resultado.get(0).getIdReserva());
    }

    @Test
    public void obtenerHabitacionesDisponiblesParaServicio_DeberiaFiltrarHabitaciones() {
        Habitacion habitacion1 = habitacionRepository.save(new Habitacion());
        Habitacion habitacion2 = habitacionRepository.save(new Habitacion());
        Servicio servicio = servicioRepository.save(new Servicio());

        Reserva reservaConServicio = new Reserva(new Date(), new Date(), 2, "Confirmada", usuarioBase, habitacion1);
        reservaConServicio.getServicios().add(servicio);
        reservaRepository.save(reservaConServicio);

        Reserva reservaSinServicio = reservaRepository.save(
                new Reserva(new Date(), new Date(), 2, "Confirmada", usuarioBase, habitacion2)
        );

        List<Habitacion> resultado = reservaService.obtenerHabitacionesDisponiblesParaServicio(servicio.getIdServicio());
        Assertions.assertEquals(1, resultado.size());
        Assertions.assertEquals(habitacion2.getIdHabitacion(), resultado.get(0).getIdHabitacion());
    }
}
