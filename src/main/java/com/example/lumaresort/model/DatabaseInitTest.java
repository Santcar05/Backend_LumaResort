package com.example.lumaresort.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import com.example.lumaresort.entities.Administrador;
import com.example.lumaresort.entities.Cliente;
import com.example.lumaresort.entities.Comentario;
import com.example.lumaresort.entities.ERole;
import com.example.lumaresort.entities.Habitacion;
import com.example.lumaresort.entities.Operador;
import com.example.lumaresort.entities.Reserva;
import com.example.lumaresort.entities.Role;
import com.example.lumaresort.entities.Servicio;
import com.example.lumaresort.entities.TipoHabitacion;
import com.example.lumaresort.entities.Usuario;
import com.example.lumaresort.repository.AdministradorRepository;
import com.example.lumaresort.repository.ClienteRepository;
import com.example.lumaresort.repository.CuentaHabitacionRepository;
import com.example.lumaresort.repository.HabitacionRepository;
import com.example.lumaresort.repository.OperadorRepository;
import com.example.lumaresort.repository.ReservaRepository;
import com.example.lumaresort.repository.RoleRepository;
import com.example.lumaresort.repository.ServicioRepository;
import com.example.lumaresort.repository.TipoHabitacionRepository;
import com.example.lumaresort.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Controller
@Transactional
@Profile("test")
public class DatabaseInitTest implements ApplicationRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ServicioRepository servicioRepository;
    @Autowired
    private HabitacionRepository habitacionRepository;
    @Autowired
    private TipoHabitacionRepository tipoHabitacionRepository;
    @Autowired
    private CuentaHabitacionRepository cuentaHabitacionRepository;
    @Autowired
    private AdministradorRepository administradorRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private OperadorRepository operadorRepository;
    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(org.springframework.boot.ApplicationArguments args) throws Exception {
        // Aquí puedes agregar la lógica para inicializar la base de datos
        // Por ejemplo, crear tablas, insertar datos iniciales, etc.
        init();
    }

    public void init() {
        // ============================
        //  CREAR ROLES
        // ============================
        Role roleCliente = roleRepository.findByNombre(ERole.ROLE_CLIENTE)
                .orElseGet(() -> roleRepository.save(new Role(ERole.ROLE_CLIENTE)));

        Role roleOperador = roleRepository.findByNombre(ERole.ROLE_OPERADOR)
                .orElseGet(() -> roleRepository.save(new Role(ERole.ROLE_OPERADOR)));

        Role roleAdministrador = roleRepository.findByNombre(ERole.ROLE_ADMINISTRADOR)
                .orElseGet(() -> roleRepository.save(new Role(ERole.ROLE_ADMINISTRADOR)));

        // ============================
        //  CREAR TIPOS DE HABITACIÓN
        // ============================
        TipoHabitacion tipo1 = TipoHabitacion.builder()
                .nombre("Individual")
                .descripcion("Habitación para una persona")
                .imagenes(java.util.Arrays.asList("https://example.com/imagen_individual.jpg"))
                .caracteristicas(java.util.Arrays.asList("Cama individual, Baño privado, Wi-Fi gratuito"))
                .precio(50.0)
                .build();

        TipoHabitacion tipo2 = TipoHabitacion.builder()
                .nombre("Doble")
                .descripcion("Habitación para dos personas")
                .imagenes(java.util.Arrays.asList("https://example.com/imagen_doble.jpg"))
                .caracteristicas(java.util.Arrays.asList("Cama doble, Baño privado, Wi-Fi gratuito, TV por cable"))
                .precio(100.0)
                .build();

        TipoHabitacion tipo3 = TipoHabitacion.builder()
                .nombre("Suite")
                .descripcion("Habitación de lujo con sala de estar")
                .imagenes(java.util.Arrays.asList("https://example.com/imagen_suite.jpg"))
                .caracteristicas(java.util.Arrays.asList("Cama king size, Sala de estar, Baño con jacuzzi, Wi-Fi gratuito, TV por cable"))
                .precio(200.0)
                .build();

        TipoHabitacion tipo4 = TipoHabitacion.builder()
                .nombre("Familiar")
                .descripcion("Habitación para toda la familia")
                .imagenes(java.util.Arrays.asList("https://example.com/imagen_familiar.jpg"))
                .caracteristicas(java.util.Arrays.asList("Dos camas dobles, Sofá cama, Baño privado, Wi-Fi gratuito, TV por cable"))
                .precio(300.0)
                .build();

        TipoHabitacion tipo5 = TipoHabitacion.builder()
                .nombre("Presidencial")
                .descripcion("Habitación de máxima categoría")
                .imagenes(java.util.Arrays.asList("https://example.com/imagen_presidencial.jpg"))
                .caracteristicas(java.util.Arrays.asList("Cama king size, Sala de estar, Comedor, Cocina pequeña, Baño con jacuzzi y ducha separada, Wi-Fi gratuito, TV por cable"))
                .precio(500.0)
                .build();

        tipoHabitacionRepository.save(tipo1);
        tipoHabitacionRepository.save(tipo2);
        tipoHabitacionRepository.save(tipo3);
        tipoHabitacionRepository.save(tipo4);
        tipoHabitacionRepository.save(tipo5);

        // ============================
        //  CREAR HABITACIONES (simplificado para TEST)
        // ============================
        Habitacion habitacion1 = Habitacion.builder()
                .numero("101")
                .precioPorNoche(100.0f)
                .estado("Ocupada")
                .capacidad(1)
                .descripcion("Habitación individual cómoda")
                .tipoHabitacion(tipo1)
                .build();

        Habitacion habitacion2 = Habitacion.builder()
                .numero("102")
                .precioPorNoche(150.0f)
                .estado("Disponible")
                .capacidad(2)
                .descripcion("Habitación doble con vista al mar")
                .tipoHabitacion(tipo2)
                .build();

        Habitacion habitacion3 = Habitacion.builder()
                .numero("201")
                .precioPorNoche(300.0f)
                .estado("Ocupada")
                .capacidad(4)
                .descripcion("Suite de lujo con jacuzzi")
                .tipoHabitacion(tipo3)
                .build();

        Habitacion habitacion4 = Habitacion.builder()
                .numero("202")
                .precioPorNoche(200.0f)
                .estado("Disponible")
                .capacidad(4)
                .descripcion("Habitación familiar espaciosa")
                .tipoHabitacion(tipo4)
                .build();

        Habitacion habitacion5 = Habitacion.builder()
                .numero("301")
                .precioPorNoche(500.0f)
                .estado("Ocupada")
                .capacidad(2)
                .descripcion("Habitación presidencial con todas las comodidades")
                .tipoHabitacion(tipo5)
                .build();

        habitacionRepository.saveAll(List.of(habitacion1, habitacion2, habitacion3, habitacion4, habitacion5));

        // ============================
        //  CREAR USUARIOS CLIENTES
        // ============================
        for (int i = 1; i <= 10; i++) {
            Usuario usuario = Usuario.builder()
                    .nombre("Usuario" + i)
                    .apellido("Test")
                    .correo("usuario" + i + "@gmail.com")
                    .contrasena(passwordEncoder.encode("pass" + i))
                    .cedula("100000000" + i)
                    .telefono("300000000" + i)
                    .roles(new ArrayList<>(List.of(roleCliente)))
                    .build();
            Usuario savedUsuario = usuarioRepository.save(usuario);

            // Crear perfil de cliente
            Cliente cliente = new Cliente();
            cliente.setUsuario(savedUsuario);
            clienteRepository.save(cliente);
        }

        // ============================
        //  CREAR USUARIOS OPERADORES
        // ============================
        for (int i = 1; i <= 5; i++) {
            Usuario usuario = Usuario.builder()
                    .nombre("Operador" + i)
                    .apellido("Soporte")
                    .correo("operador" + i + "@gmail.com")
                    .contrasena(passwordEncoder.encode("op" + i))
                    .cedula("200000000" + i)
                    .telefono("310000000" + i)
                    .roles(new ArrayList<>(List.of(roleOperador)))
                    .build();
            Usuario savedUsuario = usuarioRepository.save(usuario);

            // Crear perfil de operador
            Operador operador = new Operador();
            operador.setUsuario(savedUsuario);
            operadorRepository.save(operador);
        }

        // ============================
        //  CREAR USUARIOS ADMINISTRADORES
        // ============================
        for (int i = 1; i <= 5; i++) {
            Usuario usuario = Usuario.builder()
                    .nombre("Admin" + i)
                    .apellido("Luma")
                    .correo("admin" + i + "@gmail.com")
                    .contrasena(passwordEncoder.encode("admin" + i))
                    .cedula("300000000" + i)
                    .telefono("320000000" + i)
                    .roles(new ArrayList<>(List.of(roleAdministrador)))
                    .build();
            Usuario savedUsuario = usuarioRepository.save(usuario);

            // Crear perfil de administrador
            Administrador admin = new Administrador();
            admin.setUsuario(savedUsuario);
            administradorRepository.save(admin);
        }

        //Crear 10 CuentaHabitacion
        /* 
        CuentaHabitacion cuenta1 = new CuentaHabitacion(10.0f, null, null, null);
        CuentaHabitacion cuenta2 = new CuentaHabitacion(10.0f, null, null, null);
        CuentaHabitacion cuenta3 = new CuentaHabitacion(20.20f, null, null, null);
        CuentaHabitacion cuenta4 = new CuentaHabitacion(30.0f, null, null, null);
        CuentaHabitacion cuenta5 = new CuentaHabitacion(40.0f, null, null, null);
        CuentaHabitacion cuenta6 = new CuentaHabitacion(50.0f, null, null, null);
        CuentaHabitacion cuenta7 = new CuentaHabitacion(60.0f, null, null, null);
        CuentaHabitacion cuenta8 = new CuentaHabitacion(70.0f, null, null, null);
        CuentaHabitacion cuenta9 = new CuentaHabitacion(80.0f, null, null, null);
        CuentaHabitacion cuenta10 = new CuentaHabitacion(90.0f, null, null, null);

        cuentaHabitacionRepository.save(cuenta1);
        cuentaHabitacionRepository.save(cuenta2);
        cuentaHabitacionRepository.save(cuenta3);
        cuentaHabitacionRepository.save(cuenta4);
        cuentaHabitacionRepository.save(cuenta5);
        cuentaHabitacionRepository.save(cuenta6);
        cuentaHabitacionRepository.save(cuenta7);
        cuentaHabitacionRepository.save(cuenta8);
        cuentaHabitacionRepository.save(cuenta9);
        cuentaHabitacionRepository.save(cuenta10);
         */
        //Crar 50 comentarios
        // nombre, tipo, descripcion, precio, imagenURL
        Servicio servicio1 = new Servicio(
                "Spa",
                "Spa",
                "Relájate con nuestro servicio de spa completo que incluye sauna, jacuzzi y aromaterapia",
                50.0f,
                "https://images.pexels.com/photos/6621436/pexels-photo-6621436.jpeg"
        );

        Servicio servicio2 = new Servicio(
                "Desayuno Buffet",
                "Comida",
                "Comienza tu día con un desayuno buffet internacional ",
                20.0f,
                "https://images.pexels.com/photos/70497/pexels-photo-70497.jpeg"
        );

        Servicio servicio3 = new Servicio(
                "Cena Romántica",
                "Comida",
                "Vive una cena romántica inolvidable para dos, con un menú gourmet seleccionada por nuestro sommelier",
                80.0f,
                "https://images.pexels.com/photos/169194/pexels-photo-169194.jpeg"
        );

        Servicio servicio4 = new Servicio(
                "Tour por la Ciudad",
                "Tour",
                "Explora los atractivos turísticos junto con guías expertos que comparten la historia",
                40.0f,
                "https://images.pexels.com/photos/460672/pexels-photo-460672.jpeg"
        );

        Servicio servicio5 = new Servicio(
                "Masaje Relajante",
                "Bienestar",
                "Alivia el estrés y revitaliza tu cuerpo con un masaje profesional en nuestras cabinas privadas",
                60.0f,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQYqJtParjn0rPjqKDAFM_sapOMDhaWppczSQ&s"
        );

        Servicio servicio6 = new Servicio(
                "Clase de Yoga",
                "Bienestar",
                "Conéctate contigo mismo en nuestras clases de yoga al aire libre, realizadas al amanecer frente al mar.",
                30.0f,
                "https://images.pexels.com/photos/3823039/pexels-photo-3823039.jpeg"
        );

        Servicio servicio7 = new Servicio(
                "Alquiler de Bicicletas",
                "Transporte",
                "Descubre la ciudad y sus alrededores a tu propio ritmo con nuestro servicio de bicicletas",
                15.0f,
                "https://images.pexels.com/photos/276517/pexels-photo-276517.jpeg"
        );

        Servicio servicio8 = new Servicio(
                "Cena de Comida Internacional",
                "Comida",
                "Disfruta una cena con platillos de distintas culturas preparados por chefs",
                100.0f,
                "https://images.pexels.com/photos/1640770/pexels-photo-1640770.jpeg"
        );

        Servicio servicio9 = new Servicio(
                "Cena de Comida Local",
                "Comida",
                "Saborea lo mejor de la gastronomía regional en una cena auténtica que incluye entradas tradicionales",
                100.0f,
                "https://images.pexels.com/photos/628776/pexels-photo-628776.jpeg"
        );

        Servicio servicio10 = new Servicio(
                "Cena de Comida Internacional",
                "Comida",
                "Vive una experiencia culinaria sofisticada y una degustación gourmet",
                100.0f,
                "https://images.pexels.com/photos/958545/pexels-photo-958545.jpeg"
        );

        //Crear 5 comentarios por servicio
        /*
         public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idComentario;

    private String comentario;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    private float calificacion;

    // Relación con Servicio
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "servicio_id", referencedColumnName = "idServicio")
    private Servicio servicio;
}

         */
        Comentario comentario1Servicio1 = new Comentario(
                "Excelente servicio, recomendado!",
                new Date(),
                5.0f,
                servicio1
        );

        Comentario comentario2Servicio1 = new Comentario(
                "Muy buen servicio, recomendado!",
                new Date(),
                4.0f,
                servicio1
        );

        Comentario comentario3Servicio1 = new Comentario(
                "Buen servicio, recomendado!",
                new Date(),
                3.0f,
                servicio1
        );

        Comentario comentario1Servicio2 = new Comentario(
                "Excelente servicio, super recomendado!",
                new Date(),
                5.0f,
                servicio2
        );

        Comentario comentario2Servicio2 = new Comentario(
                "Muy buen servicio, recomendado!",
                new Date(),
                4.0f,
                servicio2
        );

        Comentario comentario3Servicio2 = new Comentario(
                "Buen servicio, recomendado!",
                new Date(),
                3.0f,
                servicio2
        );

        Comentario comentario1Servicio3 = new Comentario(
                "Mal servicio, no recomendado!",
                new Date(),
                2.0f,
                servicio3
        );

        Comentario comentario2Servicio3 = new Comentario(
                "Muy buen servicio, recomendado!",
                new Date(),
                4.0f,
                servicio3
        );

        Comentario comentario3Servicio3 = new Comentario(
                "Buen servicio, recomendado!",
                new Date(),
                3.0f,
                servicio3
        );

        Comentario comentario1Servicio4 = new Comentario(
                "Excelente servicio, super recomendado!",
                new Date(),
                5.0f,
                servicio4
        );

        Comentario comentario2Servicio4 = new Comentario(
                "Muy buen servicio, recomendado!",
                new Date(),
                4.0f,
                servicio4
        );

        Comentario comentario3Servicio4 = new Comentario(
                "Buen servicio, recomendado!",
                new Date(),
                3.0f,
                servicio4
        );

        Comentario comentario1Servicio5 = new Comentario(
                "Excelente servicio, super recomendado!",
                new Date(),
                5.0f,
                servicio5
        );

        Comentario comentario2Servicio5 = new Comentario(
                "Muy buen servicio, recomendado!",
                new Date(),
                4.0f,
                servicio5
        );

        Comentario comentario3Servicio5 = new Comentario(
                "Buen servicio, recomendado!",
                new Date(),
                3.0f,
                servicio5
        );

        Comentario comentario1Servicio6 = new Comentario(
                "Excelente servicio, super recomendado!",
                new Date(),
                5.0f,
                servicio6
        );

        Comentario comentario2Servicio6 = new Comentario(
                "Muy buen servicio, recomendado!",
                new Date(),
                4.0f,
                servicio6
        );

        Comentario comentario3Servicio6 = new Comentario(
                "Buen servicio, recomendado!",
                new Date(),
                3.0f,
                servicio6
        );

        Comentario comentario1Servicio7 = new Comentario(
                "Excelente servicio, super recomendado!",
                new Date(),
                5.0f,
                servicio7
        );

        Comentario comentario2Servicio7 = new Comentario(
                "Muy buen servicio, recomendado!",
                new Date(),
                4.0f,
                servicio7
        );

        Comentario comentario3Servicio7 = new Comentario(
                "Buen servicio, recomendado!",
                new Date(),
                3.0f,
                servicio7
        );

        Comentario comentario1Servicio8 = new Comentario(
                "Excelente servicio, super recomendado!",
                new Date(),
                5.0f,
                servicio8
        );

        Comentario comentario2Servicio8 = new Comentario(
                "Muy buen servicio, recomendado!",
                new Date(),
                4.0f,
                servicio8
        );

        Comentario comentario3Servicio8 = new Comentario(
                "Buen servicio, recomendado!",
                new Date(),
                3.0f,
                servicio8
        );

        Comentario comentario1Servicio9 = new Comentario(
                "Excelente servicio, super recomendado!",
                new Date(),
                5.0f,
                servicio9
        );

        Comentario comentario2Servicio9 = new Comentario(
                "Muy buen servicio, recomendado!",
                new Date(),
                4.0f,
                servicio9
        );

        Comentario comentario3Servicio9 = new Comentario(
                "Buen servicio, recomendado!",
                new Date(),
                3.0f,
                servicio9
        );

        Comentario comentario1Servicio10 = new Comentario(
                "Excelente servicio, super recomendado!",
                new Date(),
                5.0f,
                servicio10
        );

        Comentario comentario2Servicio10 = new Comentario(
                "Muy buen servicio, recomendado!",
                new Date(),
                4.0f,
                servicio10
        );

        Comentario comentario3Servicio10 = new Comentario(
                "Buen servicio, recomendado!",
                new Date(),
                3.0f,
                servicio10
        );
        Comentario comentario4Servicio10 = new Comentario(
                "Buen servicio, recomendado!",
                new Date(),
                3.0f,
                servicio10
        );

        servicio1.setComentarios(List.of(comentario1Servicio1, comentario2Servicio1, comentario3Servicio1));
        servicio2.setComentarios(List.of(comentario1Servicio2, comentario2Servicio2, comentario3Servicio2));
        servicio3.setComentarios(List.of(comentario1Servicio3, comentario2Servicio3, comentario3Servicio3));
        servicio4.setComentarios(List.of(comentario1Servicio4, comentario2Servicio4, comentario3Servicio4));
        servicio5.setComentarios(List.of(comentario1Servicio5, comentario2Servicio5, comentario3Servicio5));
        servicio6.setComentarios(List.of(comentario1Servicio6, comentario2Servicio6, comentario3Servicio6));
        servicio7.setComentarios(List.of(comentario1Servicio7, comentario2Servicio7, comentario3Servicio7));
        servicio8.setComentarios(List.of(comentario1Servicio8, comentario2Servicio8, comentario3Servicio8));
        servicio9.setComentarios(List.of(comentario1Servicio9, comentario2Servicio9, comentario3Servicio9));
        servicio10.setComentarios(List.of(comentario1Servicio10, comentario2Servicio10, comentario3Servicio10, comentario4Servicio10));
        //Crear 10 servicios
        // public Servicio(String nombre, String descripcion, float precio, String imagenURL, List<Comentario> comentarios, CuentaHabitacion cuentaHabitacion)
        servicioRepository.save(servicio1);
        servicioRepository.save(servicio2);
        servicioRepository.save(servicio3);
        servicioRepository.save(servicio4);
        servicioRepository.save(servicio5);
        servicioRepository.save(servicio6);
        servicioRepository.save(servicio7);
        servicioRepository.save(servicio8);
        servicioRepository.save(servicio9);
        servicioRepository.save(servicio10);

        //Crear 5 operadores
        /*
        @Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Operador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idOperador;

    @OneToOne
    @JoinColumn(name = "idUsuario", nullable = false, unique = true)
    private Usuario usuario;
}
} */
 /* 
        Operador operador1 = new Operador(
                1, usuarioRepository.findByCorreoAndContrasena("Operador1@gmail.com", "op1")
        );

        Operador operador2 = new Operador(
                2, usuarioRepository.findByCorreoAndContrasena("Operador2@gmail.com", "op2")
        );

        Operador operador3 = new Operador(
                3, usuarioRepository.findByCorreoAndContrasena("Operador3@gmail.com", "op3")
        );

        Operador operador4 = new Operador(
                4, usuarioRepository.findByCorreoAndContrasena("Operador4@gmail.com", "op4")
        );

        Operador operador5 = new Operador(
                5, usuarioRepository.findByCorreoAndContrasena("Operador5@gmail.com", "op5")
        );

        operadorRepository.save(operador1);
        operadorRepository.save(operador2);
        operadorRepository.save(operador3);
        operadorRepository.save(operador4);
        operadorRepository.save(operador5);

         */
 /*
          @Table(name = "reservas")
                public class Reserva {

                @Id
                @GeneratedValue(strategy = GenerationType.IDENTITY)
                private Integer idReserva;

                @Temporal(TemporalType.DATE) // o TIMESTAMP si quieres fecha y hora
                private Date fechaInicio;

                @Temporal(TemporalType.DATE)
                private Date fechaFin;

                private Integer cantidadPersonas;

                private String estado;

                // Relación con Cliente: muchas reservas pueden pertenecer a un cliente
                @JsonIgnore
                @ManyToOne
                @JoinColumn(name = "cliente_id", referencedColumnName = "idCliente")
                private Cliente cliente;
                }
         */
        // ============================
        //  CREAR RESERVAS
        // ============================
        Usuario usuario1 = usuarioRepository.findByCorreo("usuario1@gmail.com");
        Usuario usuario2 = usuarioRepository.findByCorreo("usuario2@gmail.com");
        Usuario usuario3 = usuarioRepository.findByCorreo("usuario3@gmail.com");

        Reserva reserva1 = new Reserva(new Date(2023 - 1900, 10, 1), new Date(2023 - 1900, 10, 5), 2, "CONFIRMADA",
                usuario1, habitacion1);
        Reserva reserva2 = new Reserva(new Date(2023 - 1900, 11, 10), new Date(2023 - 1900, 11, 15), 4, "PENDIENTE",
                usuario2, habitacion2);
        Reserva reserva3 = new Reserva(new Date(2023 - 1900, 12, 20), new Date(2023 - 1900, 12, 25), 1, "CANCELADA",
                usuario3, habitacion3);
        Reserva reserva4 = new Reserva(new Date(2024 - 1900, 1, 5), new Date(2024 - 1900, 1, 10), 3, "CONFIRMADA",
                usuario1, habitacion4);
        Reserva reserva5 = new Reserva(new Date(2024 - 1900, 2, 14), new Date(2024 - 1900, 2, 18), 2, "PENDIENTE",
                usuario2, habitacion5);

        reservaRepository.saveAll(List.of(reserva1, reserva2, reserva3, reserva4, reserva5));

        System.out.println("Inicialización de base de datos TEST completada");

    }

}
