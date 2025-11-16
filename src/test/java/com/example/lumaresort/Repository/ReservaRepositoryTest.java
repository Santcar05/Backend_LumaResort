package com.example.lumaresort.Repository;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.lumaresort.entities.Habitacion;
import com.example.lumaresort.entities.Reserva;
import com.example.lumaresort.entities.Usuario;
import com.example.lumaresort.repository.HabitacionRepository;
import com.example.lumaresort.repository.ReservaRepository;
import com.example.lumaresort.repository.UsuarioRepository;

@DataJpaTest
public class ReservaRepositoryTest {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private HabitacionRepository habitacionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Datos de prueba compartidos entre todos los tests
    private Usuario usuario;
    private Habitacion habitacion;
    private Reserva reserva;

    /**
     * Configuración inicial común para todas las pruebas Se ejecuta ANTES de
     * cada test method
     */
    @BeforeEach
    public void setUp() {
        // Crear y guardar entidades base
        usuario = new Usuario();
        habitacion = new Habitacion();

        usuarioRepository.save(usuario);
        habitacionRepository.save(habitacion);

        // Crear reserva base para pruebas
        reserva = new Reserva(
                new Date(2023, 10, 1),
                new Date(2023, 10, 5),
                2,
                "CONFIRMADA",
                usuario,
                habitacion
        );
        reservaRepository.save(reserva);
    }

    @Test
    public void test_Create() {
        // Act - Guardar nueva reserva
        Reserva nuevaReserva = new Reserva(
                new Date(2023, 11, 1),
                new Date(2023, 11, 5),
                3,
                "PENDIENTE",
                usuario,
                habitacion
        );
        Reserva reservaGuardada = reservaRepository.save(nuevaReserva);

        // Assert - Verificar que se creó con ID
        Assertions.assertNotNull(reservaGuardada.getIdReserva());
    }

    @Test
    public void test_FindAll() {
        // Arrange - Agregar más reservas
        Reserva reserva2 = new Reserva(
                new Date(2023, 10, 6),
                new Date(2023, 10, 10),
                2,
                "CONFIRMADA",
                usuario,
                habitacion
        );
        Reserva reserva3 = new Reserva(
                new Date(2023, 10, 11),
                new Date(2023, 10, 15),
                2,
                "CONFIRMADA",
                usuario,
                habitacion
        );

        reservaRepository.saveAll(List.of(reserva2, reserva3));

        // Act - Obtener todas las reservas
        List<Reserva> reservas = reservaRepository.findAll();

        // Assert - Verificar cantidad total
        Assertions.assertEquals(3, reservas.size());
    }

    @Test
    public void test_Delete() {
        // Act - Eliminar la reserva creada en setUp
        reservaRepository.delete(reserva);

        // Assert - Verificar que no hay reservas
        List<Reserva> reservas = reservaRepository.findAll();
        Assertions.assertEquals(0, reservas.size());
    }

    @Test
    public void test_FindById() {
        // Act - Buscar reserva por ID
        Reserva reservaEncontrada = reservaRepository
                .findById(reserva.getIdReserva())
                .orElse(null);

        // Assert - Verificar que se encontró
        Assertions.assertNotNull(reservaEncontrada);
        Assertions.assertEquals(reserva.getIdReserva(), reservaEncontrada.getIdReserva());
    }

    @Test
    public void test_Update() {
        // Act - Actualizar estado de la reserva
        reserva.setEstado("CANCELADA");
        reservaRepository.save(reserva);

        // Assert - Verificar que se actualizó
        Reserva reservaEncontrada = reservaRepository
                .findById(reserva.getIdReserva())
                .orElse(null);
        Assertions.assertEquals("CANCELADA", reservaEncontrada.getEstado());
    }
}
