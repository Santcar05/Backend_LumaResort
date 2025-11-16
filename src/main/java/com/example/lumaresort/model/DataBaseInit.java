package com.example.lumaresort.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import com.example.lumaresort.entities.*;
import com.example.lumaresort.repository.*;

import jakarta.transaction.Transactional;

@Controller
@Transactional
@Profile("default")
public class DataBaseInit implements ApplicationRunner {

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
        //  CREAR 10 TIPOS DE HABITACIÓN
        // ============================
        TipoHabitacion tipo1 = TipoHabitacion.builder()
                .nombre("Vista al Jardín")
                .descripcion("Habitación acogedora con vista a los jardines tropicales del resort")
                .imagenes(Arrays.asList("https://images.pexels.com/photos/271624/pexels-photo-271624.jpeg"))
                .caracteristicas(Arrays.asList("Cama king size", "Balcón privado", "Wi-Fi gratuito", "Aire acondicionado"))
                .precio(120.0)
                .build();

        TipoHabitacion tipo2 = TipoHabitacion.builder()
                .nombre("Vista al Mar")
                .descripcion("Habitación elegante con vista panorámica al océano")
                .imagenes(Arrays.asList("https://images.pexels.com/photos/261102/pexels-photo-261102.jpeg"))
                .caracteristicas(Arrays.asList("Cama king size", "Balcón con vista al mar", "Wi-Fi gratuito", "Minibar", "Cafetera"))
                .precio(180.0)
                .build();

        TipoHabitacion tipo3 = TipoHabitacion.builder()
                .nombre("Suite Luna de Miel")
                .descripcion("Suite romántica perfecta para parejas con jacuzzi privado")
                .imagenes(Arrays.asList("https://images.pexels.com/photos/1743229/pexels-photo-1743229.jpeg"))
                .caracteristicas(Arrays.asList("Cama king size", "Jacuzzi privado", "Champán de bienvenida", "Decoración romántica", "Vista al mar"))
                .precio(350.0)
                .build();

        TipoHabitacion tipo4 = TipoHabitacion.builder()
                .nombre("Suite Presidencial")
                .descripcion("La suite más exclusiva del resort con servicio de mayordomo")
                .imagenes(Arrays.asList("https://images.pexels.com/photos/2467285/pexels-photo-2467285.jpeg"))
                .caracteristicas(Arrays.asList("Dos habitaciones", "Sala de estar", "Comedor privado", "Terraza con piscina", "Mayordomo 24h"))
                .precio(800.0)
                .build();

        TipoHabitacion tipo5 = TipoHabitacion.builder()
                .nombre("Habitación Familiar")
                .descripcion("Amplia habitación ideal para familias con niños")
                .imagenes(Arrays.asList("https://images.pexels.com/photos/271643/pexels-photo-271643.jpeg"))
                .caracteristicas(Arrays.asList("Dos camas queen size", "Sofá cama", "Área de juegos", "Vista al jardín", "Frigobar"))
                .precio(250.0)
                .build();

        TipoHabitacion tipo6 = TipoHabitacion.builder()
                .nombre("Bungaló Playa")
                .descripcion("Bungaló exclusivo con acceso directo a la playa privada")
                .imagenes(Arrays.asList("https://images.pexels.com/photos/258154/pexels-photo-258154.jpeg"))
                .caracteristicas(Arrays.asList("Acceso directo a playa", "Hamaca privada", "Ducha exterior", "Cama king size", "Minibar premium"))
                .precio(450.0)
                .build();

        TipoHabitacion tipo7 = TipoHabitacion.builder()
                .nombre("Villa Tropical")
                .descripcion("Villa independiente rodeada de naturaleza tropical")
                .imagenes(Arrays.asList("https://images.pexels.com/photos/1134176/pexels-photo-1134176.jpeg"))
                .caracteristicas(Arrays.asList("Jardín privado", "Piscina privada", "Cocina completa", "Dos habitaciones", "Sala de estar"))
                .precio(600.0)
                .build();

        TipoHabitacion tipo8 = TipoHabitacion.builder()
                .nombre("Penthouse Océano")
                .descripcion("Penthouse en el último piso con vistas espectaculares de 360°")
                .imagenes(Arrays.asList("https://images.pexels.com/photos/1457842/pexels-photo-1457842.jpeg"))
                .caracteristicas(Arrays.asList("Terraza panorámica", "Jacuzzi en terraza", "Bar privado", "Tres habitaciones", "Vista 360°"))
                .precio(950.0)
                .build();

        TipoHabitacion tipo9 = TipoHabitacion.builder()
                .nombre("Habitación Ejecutiva")
                .descripcion("Habitación moderna con área de trabajo para viajeros de negocios")
                .imagenes(Arrays.asList("https://images.pexels.com/photos/271639/pexels-photo-271639.jpeg"))
                .caracteristicas(Arrays.asList("Escritorio ejecutivo", "Wi-Fi de alta velocidad", "Cafetera Nespresso", "Vista al mar", "Servicio express"))
                .precio(200.0)
                .build();

        TipoHabitacion tipo10 = TipoHabitacion.builder()
                .nombre("Suite Infinity")
                .descripcion("Suite de lujo con piscina infinity privada y vistas al atardecer")
                .imagenes(Arrays.asList("https://images.pexels.com/photos/2440471/pexels-photo-2440471.jpeg"))
                .caracteristicas(Arrays.asList("Piscina infinity privada", "Terraza amplia", "Sala de estar", "Vista panorámica al océano", "Servicio de mayordomo"))
                .precio(750.0)
                .build();

        tipoHabitacionRepository.saveAll(List.of(tipo1, tipo2, tipo3, tipo4, tipo5, tipo6, tipo7, tipo8, tipo9, tipo10));

        // ============================
        //  CREAR 100 HABITACIONES
        // ============================
        List<Habitacion> habitaciones = new ArrayList<>();
        List<TipoHabitacion> tipos = List.of(tipo1, tipo2, tipo3, tipo4, tipo5, tipo6, tipo7, tipo8, tipo9, tipo10);
        String[] estados = {"Disponible", "Ocupada", "Mantenimiento", "Reservada"};

        for (int i = 1; i <= 100; i++) {
            int piso = ((i - 1) / 10) + 1;
            int numeroHab = ((i - 1) % 10) + 1;
            String numero = String.format("%d%02d", piso, numeroHab);

            TipoHabitacion tipo = tipos.get((i - 1) % 10);
            String estado = estados[i % 4];

            Habitacion habitacion = Habitacion.builder()
                    .numero(numero)
                    .precioPorNoche(tipo.getPrecio().floatValue())
                    .estado(estado)
                    .capacidad((i % 4) + 1)
                    .descripcion(tipo.getNombre() + " - Habitación " + numero)
                    .tipoHabitacion(tipo)
                    .build();

            habitaciones.add(habitacion);
        }

        habitacionRepository.saveAll(habitaciones);

        // ============================
        //  CREAR USUARIOS CLIENTES
        // ============================
        for (int i = 1; i <= 10; i++) {
            Usuario usuario = Usuario.builder()
                    .nombre("Usuario" + i)
                    .apellido("Demo")
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

        // ============================
        // CREAR 50 SERVICIOS con 10 COMENTARIOS cada uno
        // ============================
        List<Servicio> servicios = new ArrayList<>();

        // Servicio 1: Spa Coral
        Servicio servicio1 = new Servicio(
                "Spa Coral", "Spa y Bienestar",
                "Spa de lujo con tratamientos exclusivos, sauna, baño turco y piscina termal con vista al mar",
                85.0f, "https://images.pexels.com/photos/3757942/pexels-photo-3757942.jpeg");
        List<Comentario> comentarios1 = new ArrayList<>();
        comentarios1.add(new Comentario("Experiencia increíble, muy relajante", new Date(), 5.0f, servicio1));
        comentarios1.add(new Comentario("El masaje fue excepcional", new Date(), 5.0f, servicio1));
        comentarios1.add(new Comentario("Instalaciones de primera clase", new Date(), 4.8f, servicio1));
        comentarios1.add(new Comentario("Personal muy profesional", new Date(), 4.9f, servicio1));
        comentarios1.add(new Comentario("Totalmente recomendado", new Date(), 5.0f, servicio1));
        comentarios1.add(new Comentario("Ambiente muy zen y tranquilo", new Date(), 4.7f, servicio1));
        comentarios1.add(new Comentario("Los tratamientos faciales son maravillosos", new Date(), 4.9f, servicio1));
        comentarios1.add(new Comentario("Volveré sin duda", new Date(), 5.0f, servicio1));
        comentarios1.add(new Comentario("Excelente relación calidad-precio", new Date(), 4.6f, servicio1));
        comentarios1.add(new Comentario("La mejor experiencia de spa que he tenido", new Date(), 5.0f, servicio1));
        servicio1.setComentarios(comentarios1);
        servicios.add(servicio1);

        // Servicio 2: Desayuno Buffet Caribeño
        Servicio servicio2 = new Servicio(
                "Desayuno Buffet Caribeño", "Gastronomía",
                "Buffet internacional con especialidades caribeñas, frutas tropicales frescas y estación de jugos naturales",
                35.0f, "https://images.pexels.com/photos/1640774/pexels-photo-1640774.jpeg");
        List<Comentario> comentarios2 = new ArrayList<>();
        comentarios2.add(new Comentario("Gran variedad de alimentos frescos", new Date(), 4.8f, servicio2));
        comentarios2.add(new Comentario("Las frutas tropicales estaban deliciosas", new Date(), 4.9f, servicio2));
        comentarios2.add(new Comentario("Opciones para todos los gustos", new Date(), 4.7f, servicio2));
        comentarios2.add(new Comentario("El café colombiano es excelente", new Date(), 5.0f, servicio2));
        comentarios2.add(new Comentario("Atención impecable del personal", new Date(), 4.8f, servicio2));
        comentarios2.add(new Comentario("Vista hermosa al comer", new Date(), 4.9f, servicio2));
        comentarios2.add(new Comentario("Los jugos naturales son lo mejor", new Date(), 4.8f, servicio2));
        comentarios2.add(new Comentario("Excelente para empezar el día", new Date(), 4.7f, servicio2));
        comentarios2.add(new Comentario("Hay opciones vegetarianas y veganas", new Date(), 4.6f, servicio2));
        comentarios2.add(new Comentario("Relación calidad-precio perfecta", new Date(), 4.8f, servicio2));
        servicio2.setComentarios(comentarios2);
        servicios.add(servicio2);

        // Servicio 3: Buceo en Arrecifes
        Servicio servicio3 = new Servicio(
                "Buceo en Arrecifes", "Deportes Acuáticos",
                "Inmersión guiada en los arrecifes de coral con instructor certificado, equipo incluido",
                120.0f, "https://images.pexels.com/photos/1350197/pexels-photo-1350197.jpeg");
        List<Comentario> comentarios3 = new ArrayList<>();
        comentarios3.add(new Comentario("Los arrecifes son espectaculares", new Date(), 5.0f, servicio3));
        comentarios3.add(new Comentario("Guía muy experimentado y amable", new Date(), 4.9f, servicio3));
        comentarios3.add(new Comentario("Vimos tortugas marinas", new Date(), 5.0f, servicio3));
        comentarios3.add(new Comentario("Equipo en excelente estado", new Date(), 4.8f, servicio3));
        comentarios3.add(new Comentario("Experiencia inolvidable", new Date(), 5.0f, servicio3));
        comentarios3.add(new Comentario("Perfecto para principiantes", new Date(), 4.7f, servicio3));
        comentarios3.add(new Comentario("La biodiversidad es impresionante", new Date(), 4.9f, servicio3));
        comentarios3.add(new Comentario("Fotos submarinas incluidas", new Date(), 4.8f, servicio3));
        comentarios3.add(new Comentario("Medidas de seguridad excelentes", new Date(), 5.0f, servicio3));
        comentarios3.add(new Comentario("Lo mejor del viaje", new Date(), 5.0f, servicio3));
        servicio3.setComentarios(comentarios3);
        servicios.add(servicio3);

        // Servicio 4: Masaje Piedras Calientes
        Servicio servicio4 = new Servicio(
                "Masaje Piedras Calientes", "Spa y Bienestar",
                "Terapia de relajación profunda con piedras volcánicas calientes y aceites aromáticos",
                95.0f, "https://images.pexels.com/photos/3865792/pexels-photo-3865792.jpeg");
        List<Comentario> comentarios4 = new ArrayList<>();
        comentarios4.add(new Comentario("Súper relajante", new Date(), 5.0f, servicio4));
        comentarios4.add(new Comentario("Alivió todas mis tensiones", new Date(), 4.9f, servicio4));
        comentarios4.add(new Comentario("La terapeuta fue excelente", new Date(), 5.0f, servicio4));
        comentarios4.add(new Comentario("Aceites con aromas maravillosos", new Date(), 4.8f, servicio4));
        comentarios4.add(new Comentario("Me quedé dormido de lo relajado", new Date(), 5.0f, servicio4));
        comentarios4.add(new Comentario("Técnica impecable", new Date(), 4.9f, servicio4));
        comentarios4.add(new Comentario("Temperatura perfecta de las piedras", new Date(), 4.8f, servicio4));
        comentarios4.add(new Comentario("Lo repetiría cada día", new Date(), 5.0f, servicio4));
        comentarios4.add(new Comentario("Ideal después de un día de playa", new Date(), 4.7f, servicio4));
        comentarios4.add(new Comentario("Profesionalismo total", new Date(), 4.9f, servicio4));
        servicio4.setComentarios(comentarios4);
        servicios.add(servicio4);

        // Servicio 5: Cena Romántica en la Playa
        Servicio servicio5 = new Servicio(
                "Cena Romántica en la Playa", "Gastronomía",
                "Cena privada para dos en la playa con menú gourmet, velas y música en vivo",
                180.0f, "https://images.pexels.com/photos/1395967/pexels-photo-1395967.jpeg");
        List<Comentario> comentarios5 = new ArrayList<>();
        comentarios5.add(new Comentario("Noche mágica e inolvidable", new Date(), 5.0f, servicio5));
        comentarios5.add(new Comentario("Comida exquisita", new Date(), 5.0f, servicio5));
        comentarios5.add(new Comentario("Atención impecable", new Date(), 4.9f, servicio5));
        comentarios5.add(new Comentario("El ambiente con las velas es perfecto", new Date(), 5.0f, servicio5));
        comentarios5.add(new Comentario("Música en vivo hermosa", new Date(), 4.8f, servicio5));
        comentarios5.add(new Comentario("Perfecto para aniversario", new Date(), 5.0f, servicio5));
        comentarios5.add(new Comentario("El atardecer fue espectacular", new Date(), 5.0f, servicio5));
        comentarios5.add(new Comentario("Vino de excelente calidad", new Date(), 4.9f, servicio5));
        comentarios5.add(new Comentario("Nos sentimos como VIP", new Date(), 5.0f, servicio5));
        comentarios5.add(new Comentario("Vale cada peso", new Date(), 4.8f, servicio5));
        servicio5.setComentarios(comentarios5);
        servicios.add(servicio5);

        // Servicio 6: Yoga al Amanecer
        Servicio servicio6 = new Servicio(
                "Yoga al Amanecer", "Bienestar",
                "Sesión de yoga en la playa con instructor certificado, ideal para comenzar el día con energía",
                25.0f, "https://images.pexels.com/photos/3822906/pexels-photo-3822906.jpeg");
        List<Comentario> comentarios6 = new ArrayList<>();
        comentarios6.add(new Comentario("La mejor forma de comenzar el día", new Date(), 5.0f, servicio6));
        comentarios6.add(new Comentario("Instructor muy paciente", new Date(), 4.8f, servicio6));
        comentarios6.add(new Comentario("El sonido de las olas es perfecto", new Date(), 4.9f, servicio6));
        comentarios6.add(new Comentario("Niveles para todos", new Date(), 4.7f, servicio6));
        comentarios6.add(new Comentario("Ambiente tranquilo y relajante", new Date(), 4.8f, servicio6));
        comentarios6.add(new Comentario("Me ayudó mucho con el estrés", new Date(), 4.9f, servicio6));
        comentarios6.add(new Comentario("Ver el amanecer es espectacular", new Date(), 5.0f, servicio6));
        comentarios6.add(new Comentario("Mats y equipo de buena calidad", new Date(), 4.6f, servicio6));
        comentarios6.add(new Comentario("Fui todos los días de mi estancia", new Date(), 5.0f, servicio6));
        comentarios6.add(new Comentario("Precio muy accesible", new Date(), 4.8f, servicio6));
        servicio6.setComentarios(comentarios6);
        servicios.add(servicio6);

        // Servicio 7: Tour en Kayak
        Servicio servicio7 = new Servicio(
                "Tour en Kayak", "Deportes Acuáticos",
                "Recorrido en kayak por la costa con exploración de bahías escondidas",
                45.0f, "https://images.pexels.com/photos/452738/pexels-photo-452738.jpeg");
        List<Comentario> comentarios7 = new ArrayList<>();
        comentarios7.add(new Comentario("Aventura increíble", new Date(), 4.9f, servicio7));
        comentarios7.add(new Comentario("Descubrimos playas vírgenes", new Date(), 5.0f, servicio7));
        comentarios7.add(new Comentario("Guía con mucho conocimiento local", new Date(), 4.8f, servicio7));
        comentarios7.add(new Comentario("Kayaks en buen estado", new Date(), 4.7f, servicio7));
        comentarios7.add(new Comentario("Paisajes hermosos", new Date(), 4.9f, servicio7));
        comentarios7.add(new Comentario("Buen ejercicio y diversión", new Date(), 4.6f, servicio7));
        comentarios7.add(new Comentario("Agua cristalina", new Date(), 5.0f, servicio7));
        comentarios7.add(new Comentario("Duración perfecta", new Date(), 4.7f, servicio7));
        comentarios7.add(new Comentario("Incluye refrigerios", new Date(), 4.8f, servicio7));
        comentarios7.add(new Comentario("Para toda la familia", new Date(), 4.8f, servicio7));
        servicio7.setComentarios(comentarios7);
        servicios.add(servicio7);

        // Servicio 8: Bar en la Piscina
        Servicio servicio8 = new Servicio(
                "Bar en la Piscina", "Bebidas y Cocteles",
                "Servicio de bar junto a la piscina infinity con cocteles tropicales exclusivos",
                15.0f, "https://images.pexels.com/photos/1238373/pexels-photo-1238373.jpeg");
        List<Comentario> comentarios8 = new ArrayList<>();
        comentarios8.add(new Comentario("Los mojitos son los mejores", new Date(), 4.9f, servicio8));
        comentarios8.add(new Comentario("Servicio rápido y amable", new Date(), 4.8f, servicio8));
        comentarios8.add(new Comentario("Gran variedad de tragos", new Date(), 4.7f, servicio8));
        comentarios8.add(new Comentario("Bartenders muy creativos", new Date(), 4.9f, servicio8));
        comentarios8.add(new Comentario("Ambiente perfecto", new Date(), 4.8f, servicio8));
        comentarios8.add(new Comentario("Precios razonables", new Date(), 4.6f, servicio8));
        comentarios8.add(new Comentario("Las piñas coladas son espectaculares", new Date(), 5.0f, servicio8));
        comentarios8.add(new Comentario("Música de fondo agradable", new Date(), 4.7f, servicio8));
        comentarios8.add(new Comentario("También tienen snacks", new Date(), 4.5f, servicio8));
        comentarios8.add(new Comentario("Atención hasta tarde", new Date(), 4.8f, servicio8));
        servicio8.setComentarios(comentarios8);
        servicios.add(servicio8);

        // Servicio 9: Snorkel en Bahía Coral
        Servicio servicio9 = new Servicio(
                "Snorkel en Bahía Coral", "Deportes Acuáticos",
                "Exploración submarina en aguas cristalinas con abundante vida marina",
                55.0f, "https://images.pexels.com/photos/3155726/pexels-photo-3155726.jpeg");
        List<Comentario> comentarios9 = new ArrayList<>();
        comentarios9.add(new Comentario("Vimos peces de colores increíbles", new Date(), 5.0f, servicio9));
        comentarios9.add(new Comentario("Agua super transparente", new Date(), 4.9f, servicio9));
        comentarios9.add(new Comentario("Ideal para toda la familia", new Date(), 4.8f, servicio9));
        comentarios9.add(new Comentario("Equipo de snorkel nuevo", new Date(), 4.7f, servicio9));
        comentarios9.add(new Comentario("Guía nos mostró los mejores lugares", new Date(), 4.9f, servicio9));
        comentarios9.add(new Comentario("Duración adecuada", new Date(), 4.6f, servicio9));
        comentarios9.add(new Comentario("Los niños lo disfrutaron mucho", new Date(), 4.8f, servicio9));
        comentarios9.add(new Comentario("Fotos bajo el agua incluidas", new Date(), 4.9f, servicio9));
        comentarios9.add(new Comentario("Experiencia segura", new Date(), 5.0f, servicio9));
        comentarios9.add(new Comentario("Buen precio", new Date(), 4.7f, servicio9));
        servicio9.setComentarios(comentarios9);
        servicios.add(servicio9);

        // Servicio 10: Gimnasio Premium
        Servicio servicio10 = new Servicio(
                "Gimnasio Premium", "Fitness",
                "Gimnasio equipado con tecnología de última generación y vista panorámica al mar",
                20.0f, "https://images.pexels.com/photos/1954524/pexels-photo-1954524.jpeg");
        List<Comentario> comentarios10 = new ArrayList<>();
        comentarios10.add(new Comentario("Equipamiento de primera", new Date(), 4.9f, servicio10));
        comentarios10.add(new Comentario("Vista motivadora al entrenar", new Date(), 4.8f, servicio10));
        comentarios10.add(new Comentario("Muy limpio y ordenado", new Date(), 4.9f, servicio10));
        comentarios10.add(new Comentario("Máquinas modernas", new Date(), 4.7f, servicio10));
        comentarios10.add(new Comentario("Aire acondicionado excelente", new Date(), 4.8f, servicio10));
        comentarios10.add(new Comentario("Hay entrenador disponible", new Date(), 4.6f, servicio10));
        comentarios10.add(new Comentario("Amplio y espacioso", new Date(), 4.7f, servicio10));
        comentarios10.add(new Comentario("Toallas limpias disponibles", new Date(), 4.8f, servicio10));
        comentarios10.add(new Comentario("Horario 24/7", new Date(), 5.0f, servicio10));
        comentarios10.add(new Comentario("Agua gratis", new Date(), 4.5f, servicio10));
        servicio10.setComentarios(comentarios10);
        servicios.add(servicio10);

        // Servicio 11: Clases de Surf
        Servicio servicio11 = new Servicio(
                "Clases de Surf", "Deportes Acuáticos",
                "Lecciones de surf con instructores profesionales en las mejores olas de la zona",
                65.0f, "https://images.pexels.com/photos/390051/surfer-wave-sunset-the-indian-ocean-390051.jpeg");
        List<Comentario> comentarios11 = new ArrayList<>();
        comentarios11.add(new Comentario("Aprendí a surfear en mi primera clase", new Date(), 5.0f, servicio11));
        comentarios11.add(new Comentario("Instructores súper pacientes", new Date(), 4.9f, servicio11));
        comentarios11.add(new Comentario("Olas perfectas para principiantes", new Date(), 4.8f, servicio11));
        comentarios11.add(new Comentario("Tablas en excelente estado", new Date(), 4.7f, servicio11));
        comentarios11.add(new Comentario("Adrenalina pura", new Date(), 5.0f, servicio11));
        comentarios11.add(new Comentario("Grupos pequeños", new Date(), 4.8f, servicio11));
        comentarios11.add(new Comentario("Tomaron fotos de mi primera ola", new Date(), 4.9f, servicio11));
        comentarios11.add(new Comentario("Medidas de seguridad top", new Date(), 4.7f, servicio11));
        comentarios11.add(new Comentario("Definitivamente volveré", new Date(), 5.0f, servicio11));
        comentarios11.add(new Comentario("Buen valor por el precio", new Date(), 4.6f, servicio11));
        servicio11.setComentarios(comentarios11);
        servicios.add(servicio11);

        // Servicio 12: Restaurante Gourmet Mar y Tierra
        Servicio servicio12 = new Servicio(
                "Restaurante Gourmet Mar y Tierra", "Gastronomía",
                "Restaurante de alta cocina con mariscos frescos y carnes premium",
                75.0f, "https://images.pexels.com/photos/262978/pexels-photo-262978.jpeg");
        List<Comentario> comentarios12 = new ArrayList<>();
        comentarios12.add(new Comentario("Langosta espectacular", new Date(), 5.0f, servicio12));
        comentarios12.add(new Comentario("Chef con mucho talento", new Date(), 4.9f, servicio12));
        comentarios12.add(new Comentario("Presentación de platos impecable", new Date(), 5.0f, servicio12));
        comentarios12.add(new Comentario("Vinos de la mejor selección", new Date(), 4.8f, servicio12));
        comentarios12.add(new Comentario("Servicio de meseros excelente", new Date(), 4.9f, servicio12));
        comentarios12.add(new Comentario("Ambiente elegante", new Date(), 4.7f, servicio12));
        comentarios12.add(new Comentario("El atún está increíble", new Date(), 5.0f, servicio12));
        comentarios12.add(new Comentario("Postres deliciosos", new Date(), 4.8f, servicio12));
        comentarios12.add(new Comentario("Reserva obligatoria", new Date(), 4.6f, servicio12));
        comentarios12.add(new Comentario("Una experiencia culinaria única", new Date(), 5.0f, servicio12));
        servicio12.setComentarios(comentarios12);
        servicios.add(servicio12);

        // Servicio 13: Parasailing
        Servicio servicio13 = new Servicio(
                "Parasailing", "Deportes Acuáticos",
                "Vuelo en paracaídas sobre el océano con vistas panorámicas impresionantes",
                90.0f, "https://images.pexels.com/photos/848618/pexels-photo-848618.jpeg");
        List<Comentario> comentarios13 = new ArrayList<>();
        comentarios13.add(new Comentario("Vistas increíbles desde arriba", new Date(), 5.0f, servicio13));
        comentarios13.add(new Comentario("Adrenalina al máximo", new Date(), 4.9f, servicio13));
        comentarios13.add(new Comentario("Personal muy profesional", new Date(), 4.8f, servicio13));
        comentarios13.add(new Comentario("Equipo de seguridad confiable", new Date(), 5.0f, servicio13));
        comentarios13.add(new Comentario("Experiencia única", new Date(), 4.9f, servicio13));
        comentarios13.add(new Comentario("Duración perfecta del vuelo", new Date(), 4.7f, servicio13));
        comentarios13.add(new Comentario("Se ve todo el resort desde arriba", new Date(), 5.0f, servicio13));
        comentarios13.add(new Comentario("Aterrizaje suave", new Date(), 4.8f, servicio13));
        comentarios13.add(new Comentario("Fotos y video incluidos", new Date(), 4.9f, servicio13));
        comentarios13.add(new Comentario("Lo mejor de mis vacaciones", new Date(), 5.0f, servicio13));
        servicio13.setComentarios(comentarios13);
        servicios.add(servicio13);

        // Servicio 14: Masaje Balinés
        Servicio servicio14 = new Servicio(
                "Masaje Balinés", "Spa y Bienestar",
                "Técnica tradicional de masaje con presión profunda y aromaterapia",
                80.0f, "https://images.pexels.com/photos/3757952/pexels-photo-3757952.jpeg");
        List<Comentario> comentarios14 = new ArrayList<>();
        comentarios14.add(new Comentario("Técnica maravillosa", new Date(), 5.0f, servicio14));
        comentarios14.add(new Comentario("Alivió mi dolor de espalda", new Date(), 4.9f, servicio14));
        comentarios14.add(new Comentario("Masajista muy hábil", new Date(), 5.0f, servicio14));
        comentarios14.add(new Comentario("Aceites aromáticos deliciosos", new Date(), 4.8f, servicio14));
        comentarios14.add(new Comentario("Sala de masajes hermosa", new Date(), 4.7f, servicio14));
        comentarios14.add(new Comentario("Presión perfecta", new Date(), 4.9f, servicio14));
        comentarios14.add(new Comentario("Me sentí renovado", new Date(), 5.0f, servicio14));
        comentarios14.add(new Comentario("Ambiente muy tranquilo", new Date(), 4.8f, servicio14));
        comentarios14.add(new Comentario("Duración ideal", new Date(), 4.6f, servicio14));
        comentarios14.add(new Comentario("Lo recomiendo al 100%", new Date(), 5.0f, servicio14));
        servicio14.setComentarios(comentarios14);
        servicios.add(servicio14);

        // Servicio 15: Tour de Avistamiento de Delfines
        Servicio servicio15 = new Servicio(
                "Tour de Avistamiento de Delfines", "Ecoturismo",
                "Excursión en barco para observar delfines en su hábitat natural",
                70.0f, "https://images.pexels.com/photos/1645028/pexels-photo-1645028.jpeg");
        List<Comentario> comentarios15 = new ArrayList<>();
        comentarios15.add(new Comentario("Vimos más de 20 delfines", new Date(), 5.0f, servicio15));
        comentarios15.add(new Comentario("Experiencia mágica", new Date(), 5.0f, servicio15));
        comentarios15.add(new Comentario("Guía con mucho conocimiento", new Date(), 4.8f, servicio15));
        comentarios15.add(new Comentario("Barco cómodo", new Date(), 4.7f, servicio15));
        comentarios15.add(new Comentario("Los niños quedaron encantados", new Date(), 4.9f, servicio15));
        comentarios15.add(new Comentario("Incluye refrigerios", new Date(), 4.6f, servicio15));
        comentarios15.add(new Comentario("Los delfines saltaban cerca del barco", new Date(), 5.0f, servicio15));
        comentarios15.add(new Comentario("Respeto por los animales", new Date(), 4.9f, servicio15));
        comentarios15.add(new Comentario("Tomaron fotos profesionales", new Date(), 4.8f, servicio15));
        comentarios15.add(new Comentario("Salida temprano en la mañana vale la pena", new Date(), 4.7f, servicio15));
        servicio15.setComentarios(comentarios15);
        servicios.add(servicio15);

        // Servicio 16: Club de Niños Aventura Marina
        Servicio servicio16 = new Servicio(
                "Club de Niños Aventura Marina", "Entretenimiento Infantil",
                "Club infantil con actividades supervisadas, juegos y talleres creativos",
                30.0f, "https://images.pexels.com/photos/1720186/pexels-photo-1720186.jpeg");
        List<Comentario> comentarios16 = new ArrayList<>();
        comentarios16.add(new Comentario("Los niños no querían irse", new Date(), 5.0f, servicio16));
        comentarios16.add(new Comentario("Personal muy cariñoso", new Date(), 4.9f, servicio16));
        comentarios16.add(new Comentario("Actividades muy variadas", new Date(), 4.8f, servicio16));
        comentarios16.add(new Comentario("Instalaciones seguras", new Date(), 5.0f, servicio16));
        comentarios16.add(new Comentario("Mi hijo hizo muchos amigos", new Date(), 4.7f, servicio16));
        comentarios16.add(new Comentario("Pudimos disfrutar del spa mientras", new Date(), 4.9f, servicio16));
        comentarios16.add(new Comentario("Talleres educativos y divertidos", new Date(), 4.8f, servicio16));
        comentarios16.add(new Comentario("Comidas saludables incluidas", new Date(), 4.6f, servicio16));
        comentarios16.add(new Comentario("Sistema de llamado por mensaje", new Date(), 4.7f, servicio16));
        comentarios16.add(new Comentario("Excelente supervisión", new Date(), 5.0f, servicio16));
        servicio16.setComentarios(comentarios16);
        servicios.add(servicio16);

        // Servicio 17: Clases de Cocina Caribeña
        Servicio servicio17 = new Servicio(
                "Clases de Cocina Caribeña", "Gastronomía",
                "Aprende a preparar platos típicos de la región con nuestro chef ejecutivo",
                50.0f, "https://images.pexels.com/photos/1153655/pexels-photo-1153655.jpeg");
        List<Comentario> comentarios17 = new ArrayList<>();
        comentarios17.add(new Comentario("Aprendí a hacer ceviche perfecto", new Date(), 5.0f, servicio17));
        comentarios17.add(new Comentario("Chef muy didáctico", new Date(), 4.8f, servicio17));
        comentarios17.add(new Comentario("Nos llevamos las recetas", new Date(), 4.9f, servicio17));
        comentarios17.add(new Comentario("Comimos lo que preparamos", new Date(), 4.7f, servicio17));
        comentarios17.add(new Comentario("Grupo pequeño e interactivo", new Date(), 4.8f, servicio17));
        comentarios17.add(new Comentario("Ingredientes frescos locales", new Date(), 4.9f, servicio17));
        comentarios17.add(new Comentario("Cocina profesional", new Date(), 4.6f, servicio17));
        comentarios17.add(new Comentario("También enseñan cocteles", new Date(), 4.7f, servicio17));
        comentarios17.add(new Comentario("Experiencia cultural auténtica", new Date(), 5.0f, servicio17));
        comentarios17.add(new Comentario("Incluye delantal de regalo", new Date(), 4.5f, servicio17));
        servicio17.setComentarios(comentarios17);
        servicios.add(servicio17);

        // Servicio 18: Jet Ski
        Servicio servicio18 = new Servicio(
                "Jet Ski", "Deportes Acuáticos",
                "Alquiler de motos acuáticas de última generación con recorrido guiado",
                85.0f, "https://images.pexels.com/photos/1449667/pexels-photo-1449667.jpeg");
        List<Comentario> comentarios18 = new ArrayList<>();
        comentarios18.add(new Comentario("Velocidad y diversión", new Date(), 5.0f, servicio18));
        comentarios18.add(new Comentario("Jet skis nuevos", new Date(), 4.9f, servicio18));
        comentarios18.add(new Comentario("Guía nos llevó a lugares increíbles", new Date(), 4.8f, servicio18));
        comentarios18.add(new Comentario("Fácil de manejar", new Date(), 4.7f, servicio18));
        comentarios18.add(new Comentario("Agua cristalina", new Date(), 4.9f, servicio18));
        comentarios18.add(new Comentario("Instrucciones claras de seguridad", new Date(), 4.8f, servicio18));
        comentarios18.add(new Comentario("Llegamos a una cueva marina", new Date(), 5.0f, servicio18));
        comentarios18.add(new Comentario("Chalecos salvavidas cómodos", new Date(), 4.6f, servicio18));
        comentarios18.add(new Comentario("Hora de duración perfecta", new Date(), 4.7f, servicio18));
        comentarios18.add(new Comentario("Adrenalina garantizada", new Date(), 5.0f, servicio18));
        servicio18.setComentarios(comentarios18);
        servicios.add(servicio18);

        // Servicio 19: Tratamiento Facial con Algas Marinas
        Servicio servicio19 = new Servicio(
                "Tratamiento Facial con Algas Marinas", "Spa y Bienestar",
                "Tratamiento facial hidratante y rejuvenecedor con productos del mar",
                65.0f, "https://images.pexels.com/photos/3764013/pexels-photo-3764013.jpeg");
        List<Comentario> comentarios19 = new ArrayList<>();
        comentarios19.add(new Comentario("Mi piel quedó radiante", new Date(), 5.0f, servicio19));
        comentarios19.add(new Comentario("Productos naturales de alta calidad", new Date(), 4.9f, servicio19));
        comentarios19.add(new Comentario("Esteticista muy profesional", new Date(), 4.8f, servicio19));
        comentarios19.add(new Comentario("Efecto inmediato", new Date(), 4.7f, servicio19));
        comentarios19.add(new Comentario("Olores marinos agradables", new Date(), 4.6f, servicio19));
        comentarios19.add(new Comentario("Masaje facial incluido", new Date(), 4.9f, servicio19));
        comentarios19.add(new Comentario("Ambiente super relajante", new Date(), 4.8f, servicio19));
        comentarios19.add(new Comentario("Hidratación profunda", new Date(), 5.0f, servicio19));
        comentarios19.add(new Comentario("Perfecto después del sol", new Date(), 4.7f, servicio19));
        comentarios19.add(new Comentario("Buen precio para la calidad", new Date(), 4.8f, servicio19));
        servicio19.setComentarios(comentarios19);
        servicios.add(servicio19);

        // Servicio 20: Paseo a Caballo por la Playa
        Servicio servicio20 = new Servicio(
                "Paseo a Caballo por la Playa", "Ecoturismo",
                "Cabalgata al atardecer por la playa y bosque tropical",
                60.0f, "https://images.pexels.com/photos/3714898/pexels-photo-3714898.jpeg");
        List<Comentario> comentarios20 = new ArrayList<>();
        comentarios20.add(new Comentario("Atardecer espectacular a caballo", new Date(), 5.0f, servicio20));
        comentarios20.add(new Comentario("Caballos muy bien cuidados", new Date(), 4.9f, servicio20));
        comentarios20.add(new Comentario("Guía experto y amable", new Date(), 4.8f, servicio20));
        comentarios20.add(new Comentario("Apto para principiantes", new Date(), 4.7f, servicio20));
        comentarios20.add(new Comentario("Paisajes hermosos", new Date(), 5.0f, servicio20));
        comentarios20.add(new Comentario("Caballos tranquilos", new Date(), 4.8f, servicio20));
        comentarios20.add(new Comentario("Recorrido por el bosque increíble", new Date(), 4.9f, servicio20));
        comentarios20.add(new Comentario("Duración ideal", new Date(), 4.6f, servicio20));
        comentarios20.add(new Comentario("Cascos de seguridad incluidos", new Date(), 4.7f, servicio20));
        comentarios20.add(new Comentario("Experiencia romántica", new Date(), 5.0f, servicio20));
        servicio20.setComentarios(comentarios20);
        servicios.add(servicio20);

        // Servicio 21: Fiesta en la Piscina
        Servicio servicio21 = new Servicio(
                "Fiesta en la Piscina", "Entretenimiento",
                "Fiesta nocturna con DJ, luces y animación en la piscina principal",
                25.0f, "https://images.pexels.com/photos/1190297/pexels-photo-1190297.jpeg");
        List<Comentario> comentarios21 = new ArrayList<>();
        comentarios21.add(new Comentario("Fiesta increíble", new Date(), 5.0f, servicio21));
        comentarios21.add(new Comentario("DJ con buena música", new Date(), 4.8f, servicio21));
        comentarios21.add(new Comentario("Ambiente muy alegre", new Date(), 4.9f, servicio21));
        comentarios21.add(new Comentario("Conocimos gente genial", new Date(), 4.7f, servicio21));
        comentarios21.add(new Comentario("Luces espectaculares", new Date(), 4.8f, servicio21));
        comentarios21.add(new Comentario("Bebidas a buen precio", new Date(), 4.6f, servicio21));
        comentarios21.add(new Comentario("Animadores muy divertidos", new Date(), 4.7f, servicio21));
        comentarios21.add(new Comentario("Juegos en el agua", new Date(), 4.8f, servicio21));
        comentarios21.add(new Comentario("Cada viernes es temática diferente", new Date(), 4.9f, servicio21));
        comentarios21.add(new Comentario("No te lo puedes perder", new Date(), 5.0f, servicio21));
        servicio21.setComentarios(comentarios21);
        servicios.add(servicio21);

        // Servicio 22: Windsurf
        Servicio servicio22 = new Servicio(
                "Windsurf", "Deportes Acuáticos",
                "Clases y alquiler de equipo de windsurf con instructores certificados",
                70.0f, "https://images.pexels.com/photos/2272939/pexels-photo-2272939.jpeg");
        List<Comentario> comentarios22 = new ArrayList<>();
        comentarios22.add(new Comentario("Deporte emocionante", new Date(), 4.9f, servicio22));
        comentarios22.add(new Comentario("Instructores muy pacientes", new Date(), 4.8f, servicio22));
        comentarios22.add(new Comentario("Equipo moderno", new Date(), 4.7f, servicio22));
        comentarios22.add(new Comentario("Viento perfecto por las tardes", new Date(), 4.9f, servicio22));
        comentarios22.add(new Comentario("Logré mantenerme en la tabla", new Date(), 4.6f, servicio22));
        comentarios22.add(new Comentario("Clases en grupos pequeños", new Date(), 4.8f, servicio22));
        comentarios22.add(new Comentario("Desafiante pero divertido", new Date(), 4.7f, servicio22));
        comentarios22.add(new Comentario("Zona segura para aprender", new Date(), 4.8f, servicio22));
        comentarios22.add(new Comentario("Buena relación precio-calidad", new Date(), 4.5f, servicio22));
        comentarios22.add(new Comentario("Definitivamente volveré", new Date(), 4.9f, servicio22));
        servicio22.setComentarios(comentarios22);
        servicios.add(servicio22);

        // Servicio 23: Servicio de Spa en Habitación
        Servicio servicio23 = new Servicio(
                "Servicio de Spa en Habitación", "Spa y Bienestar",
                "Masajes y tratamientos de spa en la comodidad de tu habitación",
                110.0f, "https://images.pexels.com/photos/3997386/pexels-photo-3997386.jpeg");
        List<Comentario> comentarios23 = new ArrayList<>();
        comentarios23.add(new Comentario("Comodidad total", new Date(), 5.0f, servicio23));
        comentarios23.add(new Comentario("Terapeuta llegó puntual", new Date(), 4.9f, servicio23));
        comentarios23.add(new Comentario("Trajeron camilla profesional", new Date(), 4.8f, servicio23));
        comentarios23.add(new Comentario("Privacidad garantizada", new Date(), 5.0f, servicio23));
        comentarios23.add(new Comentario("Música relajante", new Date(), 4.7f, servicio23));
        comentarios23.add(new Comentario("Perfecto para parejas", new Date(), 4.9f, servicio23));
        comentarios23.add(new Comentario("Técnica impecable", new Date(), 4.8f, servicio23));
        comentarios23.add(new Comentario("Aceites de excelente calidad", new Date(), 4.9f, servicio23));
        comentarios23.add(new Comentario("No tienes que moverte", new Date(), 5.0f, servicio23));
        comentarios23.add(new Comentario("Vale la pena el costo adicional", new Date(), 4.8f, servicio23));
        servicio23.setComentarios(comentarios23);
        servicios.add(servicio23);

        // Servicio 24: Tour Fotográfico al Atardecer
        Servicio servicio24 = new Servicio(
                "Tour Fotográfico al Atardecer", "Entretenimiento",
                "Sesión fotográfica profesional en los mejores spots del resort",
                95.0f, "https://images.pexels.com/photos/1906658/pexels-photo-1906658.jpeg");
        List<Comentario> comentarios24 = new ArrayList<>();
        comentarios24.add(new Comentario("Fotos espectaculares", new Date(), 5.0f, servicio24));
        comentarios24.add(new Comentario("Fotógrafo muy profesional", new Date(), 4.9f, servicio24));
        comentarios24.add(new Comentario("Nos llevó a lugares increíbles", new Date(), 5.0f, servicio24));
        comentarios24.add(new Comentario("Luz del atardecer perfecta", new Date(), 4.8f, servicio24));
        comentarios24.add(new Comentario("Recibimos todas las fotos editadas", new Date(), 4.9f, servicio24));
        comentarios24.add(new Comentario("Poses naturales y espontáneas", new Date(), 4.7f, servicio24));
        comentarios24.add(new Comentario("Perfecto para aniversario", new Date(), 5.0f, servicio24));
        comentarios24.add(new Comentario("Entrega rápida de fotos", new Date(), 4.8f, servicio24));
        comentarios24.add(new Comentario("Calidad profesional", new Date(), 4.9f, servicio24));
        comentarios24.add(new Comentario("Recuerdo invaluable", new Date(), 5.0f, servicio24));
        servicio24.setComentarios(comentarios24);
        servicios.add(servicio24);

        // Servicio 25: Pesca Deportiva
        Servicio servicio25 = new Servicio(
                "Pesca Deportiva", "Deportes Acuáticos",
                "Salida de pesca en alta mar con equipo profesional y capitán experto",
                150.0f, "https://images.pexels.com/photos/1007025/pexels-photo-1007025.jpeg");
        List<Comentario> comentarios25 = new ArrayList<>();
        comentarios25.add(new Comentario("Pescamos un marlín", new Date(), 5.0f, servicio25));
        comentarios25.add(new Comentario("Capitán con mucha experiencia", new Date(), 4.9f, servicio25));
        comentarios25.add(new Comentario("Barco en excelente estado", new Date(), 4.8f, servicio25));
        comentarios25.add(new Comentario("Equipo de pesca profesional", new Date(), 4.9f, servicio25));
        comentarios25.add(new Comentario("Incluye refrigerios y bebidas", new Date(), 4.7f, servicio25));
        comentarios25.add(new Comentario("Vimos delfines en el camino", new Date(), 5.0f, servicio25));
        comentarios25.add(new Comentario("Limpiaron el pescado para nosotros", new Date(), 4.8f, servicio25));
        comentarios25.add(new Comentario("4 horas de pura diversión", new Date(), 4.9f, servicio25));
        comentarios25.add(new Comentario("Apto para principiantes", new Date(), 4.6f, servicio25));
        comentarios25.add(new Comentario("Experiencia inolvidable", new Date(), 5.0f, servicio25));
        servicio25.setComentarios(comentarios25);
        servicios.add(servicio25);

        // Servicio 26: Reflexología Podal
        Servicio servicio26 = new Servicio(
                "Reflexología Podal", "Spa y Bienestar",
                "Masaje terapéutico en pies y piernas con técnicas de reflexología",
                40.0f, "https://images.pexels.com/photos/6530620/pexels-photo-6530620.jpeg");
        List<Comentario> comentarios26 = new ArrayList<>();
        comentarios26.add(new Comentario("Mis pies lo necesitaban", new Date(), 5.0f, servicio26));
        comentarios26.add(new Comentario("Técnica muy efectiva", new Date(), 4.8f, servicio26));
        comentarios26.add(new Comentario("Mis pies lo necesitaban", new Date(), 5.0f, servicio26));
        comentarios26.add(new Comentario("Técnica muy efectiva", new Date(), 4.8f, servicio26));
        comentarios26.add(new Comentario("Relajación total", new Date(), 4.9f, servicio26));
        comentarios26.add(new Comentario("Terapeuta muy profesional", new Date(), 4.7f, servicio26));
        comentarios26.add(new Comentario("Alivió mi dolor de pies", new Date(), 5.0f, servicio26));
        comentarios26.add(new Comentario("Presión adecuada", new Date(), 4.6f, servicio26));
        comentarios26.add(new Comentario("Precio muy accesible", new Date(), 4.5f, servicio26));
        comentarios26.add(new Comentario("Lo repetiría", new Date(), 4.8f, servicio26));
        comentarios26.add(new Comentario("Perfecto después de caminar en la playa", new Date(), 4.9f, servicio26));
        comentarios26.add(new Comentario("Sensación de ligereza", new Date(), 4.7f, servicio26));
        servicio26.setComentarios(comentarios26);
        servicios.add(servicio26);

        // Servicio 27: Excursión en Catamarán
        Servicio servicio27 = new Servicio(
                "Excursión en Catamarán", "Deportes Acuáticos",
                "Tour de medio día en catamarán con música, bebidas y paradas para nadar",
                100.0f, "https://images.pexels.com/photos/163236/luxury-yacht-boat-speed-water-163236.jpeg");
        List<Comentario> comentarios27 = new ArrayList<>();
        comentarios27.add(new Comentario("Día perfecto en el mar", new Date(), 5.0f, servicio27));
        comentarios27.add(new Comentario("Tripulación muy amable", new Date(), 4.9f, servicio27));
        comentarios27.add(new Comentario("Bebidas ilimitadas incluidas", new Date(), 4.8f, servicio27));
        comentarios27.add(new Comentario("Música genial", new Date(), 4.7f, servicio27));
        comentarios27.add(new Comentario("Vimos una isla hermosa", new Date(), 5.0f, servicio27));
        comentarios27.add(new Comentario("Catamarán espacioso y cómodo", new Date(), 4.8f, servicio27));
        comentarios27.add(new Comentario("Snorkel incluido", new Date(), 4.9f, servicio27));
        comentarios27.add(new Comentario("Almuerzo delicioso", new Date(), 4.6f, servicio27));
        comentarios27.add(new Comentario("Ambiente muy divertido", new Date(), 4.8f, servicio27));
        comentarios27.add(new Comentario("Mejor tour del viaje", new Date(), 5.0f, servicio27));
        servicio27.setComentarios(comentarios27);
        servicios.add(servicio27);

        // Servicio 28: Manicure y Pedicure
        Servicio servicio28 = new Servicio(
                "Manicure y Pedicure", "Spa y Bienestar",
                "Servicio completo de manicure y pedicure con esmaltado permanente",
                55.0f, "https://images.pexels.com/photos/3997350/pexels-photo-3997350.jpeg");
        List<Comentario> comentarios28 = new ArrayList<>();
        comentarios28.add(new Comentario("Trabajo impecable", new Date(), 5.0f, servicio28));
        comentarios28.add(new Comentario("Gran variedad de colores", new Date(), 4.8f, servicio28));
        comentarios28.add(new Comentario("Técnica muy delicada", new Date(), 4.9f, servicio28));
        comentarios28.add(new Comentario("Duró mucho tiempo", new Date(), 4.7f, servicio28));
        comentarios28.add(new Comentario("Ambiente limpio", new Date(), 4.8f, servicio28));
        comentarios28.add(new Comentario("Personal atento", new Date(), 4.6f, servicio28));
        comentarios28.add(new Comentario("Productos de calidad", new Date(), 4.9f, servicio28));
        comentarios28.add(new Comentario("Masaje de manos incluido", new Date(), 4.8f, servicio28));
        comentarios28.add(new Comentario("Perfecto para relajarse", new Date(), 4.7f, servicio28));
        comentarios28.add(new Comentario("Excelente relación calidad-precio", new Date(), 4.8f, servicio28));
        servicio28.setComentarios(comentarios28);
        servicios.add(servicio28);

        // Servicio 29: Stand Up Paddle
        Servicio servicio29 = new Servicio(
                "Stand Up Paddle", "Deportes Acuáticos",
                "Alquiler de tablas de paddle con clases para principiantes",
                30.0f, "https://images.pexels.com/photos/2131967/pexels-photo-2131967.jpeg");
        List<Comentario> comentarios29 = new ArrayList<>();
        comentarios29.add(new Comentario("Muy divertido y relajante", new Date(), 4.8f, servicio29));
        comentarios29.add(new Comentario("Fácil de aprender", new Date(), 4.7f, servicio29));
        comentarios29.add(new Comentario("Tablas en buen estado", new Date(), 4.6f, servicio29));
        comentarios29.add(new Comentario("Buen ejercicio", new Date(), 4.8f, servicio29));
        comentarios29.add(new Comentario("Agua tranquila perfecta", new Date(), 4.9f, servicio29));
        comentarios29.add(new Comentario("Instructor paciente", new Date(), 4.7f, servicio29));
        comentarios29.add(new Comentario("Vistas hermosas", new Date(), 5.0f, servicio29));
        comentarios29.add(new Comentario("Precio accesible", new Date(), 4.6f, servicio29));
        comentarios29.add(new Comentario("Para todos los niveles", new Date(), 4.8f, servicio29));
        comentarios29.add(new Comentario("Lo haría de nuevo", new Date(), 4.7f, servicio29));
        servicio29.setComentarios(comentarios29);
        servicios.add(servicio29);

        // Servicio 30: Tour de Observación de Estrellas
        Servicio servicio30 = new Servicio(
                "Tour de Observación de Estrellas", "Ecoturismo",
                "Noche de astronomía con telescopio profesional y experto en constelaciones",
                45.0f, "https://images.pexels.com/photos/1252890/pexels-photo-1252890.jpeg");
        List<Comentario> comentarios30 = new ArrayList<>();
        comentarios30.add(new Comentario("Experiencia única", new Date(), 5.0f, servicio30));
        comentarios30.add(new Comentario("El guía sabe mucho", new Date(), 4.9f, servicio30));
        comentarios30.add(new Comentario("Vimos planetas y galaxias", new Date(), 5.0f, servicio30));
        comentarios30.add(new Comentario("Noche despejada perfecta", new Date(), 4.8f, servicio30));
        comentarios30.add(new Comentario("Telescopio potente", new Date(), 4.9f, servicio30));
        comentarios30.add(new Comentario("Incluye bebida caliente", new Date(), 4.6f, servicio30));
        comentarios30.add(new Comentario("Romántico y educativo", new Date(), 5.0f, servicio30));
        comentarios30.add(new Comentario("Ideal para familias", new Date(), 4.7f, servicio30));
        comentarios30.add(new Comentario("Aprendí sobre constelaciones", new Date(), 4.8f, servicio30));
        comentarios30.add(new Comentario("Vale la pena", new Date(), 4.9f, servicio30));
        servicio30.setComentarios(comentarios30);
        servicios.add(servicio30);

        // Servicio 31: Aromaterapia
        Servicio servicio31 = new Servicio(
                "Aromaterapia", "Spa y Bienestar",
                "Sesión de aromaterapia con aceites esenciales y masaje terapéutico",
                70.0f, "https://images.pexels.com/photos/3865583/pexels-photo-3865583.jpeg");
        List<Comentario> comentarios31 = new ArrayList<>();
        comentarios31.add(new Comentario("Olores divinos", new Date(), 5.0f, servicio31));
        comentarios31.add(new Comentario("Muy relajante", new Date(), 4.9f, servicio31));
        comentarios31.add(new Comentario("Terapeuta experta", new Date(), 4.8f, servicio31));
        comentarios31.add(new Comentario("Ambiente perfecto", new Date(), 4.9f, servicio31));
        comentarios31.add(new Comentario("Aceites de excelente calidad", new Date(), 5.0f, servicio31));
        comentarios31.add(new Comentario("Me sentí renovado", new Date(), 4.7f, servicio31));
        comentarios31.add(new Comentario("Duración ideal", new Date(), 4.8f, servicio31));
        comentarios31.add(new Comentario("Técnica profesional", new Date(), 4.9f, servicio31));
        comentarios31.add(new Comentario("Totalmente recomendado", new Date(), 5.0f, servicio31));
        comentarios31.add(new Comentario("Ayudó con mi estrés", new Date(), 4.8f, servicio31));
        servicio31.setComentarios(comentarios31);
        servicios.add(servicio31);

        // Servicio 32: Clases de Baile Latino
        Servicio servicio32 = new Servicio(
                "Clases de Baile Latino", "Entretenimiento",
                "Aprende salsa, bachata y merengue con instructores profesionales",
                35.0f, "https://images.pexels.com/photos/3621104/pexels-photo-3621104.jpeg");
        List<Comentario> comentarios32 = new ArrayList<>();
        comentarios32.add(new Comentario("Muy divertido", new Date(), 5.0f, servicio32));
        comentarios32.add(new Comentario("Instructores pacientes", new Date(), 4.8f, servicio32));
        comentarios32.add(new Comentario("Aprendí mucho", new Date(), 4.7f, servicio32));
        comentarios32.add(new Comentario("Ambiente alegre", new Date(), 4.9f, servicio32));
        comentarios32.add(new Comentario("Para todos los niveles", new Date(), 4.6f, servicio32));
        comentarios32.add(new Comentario("Música excelente", new Date(), 4.8f, servicio32));
        comentarios32.add(new Comentario("Conocimos gente nueva", new Date(), 4.7f, servicio32));
        comentarios32.add(new Comentario("Buen ejercicio", new Date(), 4.8f, servicio32));
        comentarios32.add(new Comentario("Clase dinámica", new Date(), 4.9f, servicio32));
        comentarios32.add(new Comentario("Lo repetiría", new Date(), 4.7f, servicio32));
        servicio32.setComentarios(comentarios32);
        servicios.add(servicio32);

        // Servicio 33: Banana Boat
        Servicio servicio33 = new Servicio(
                "Banana Boat", "Deportes Acuáticos",
                "Paseo extremo en banana boat con adrenalina garantizada",
                40.0f, "https://images.pexels.com/photos/2265876/pexels-photo-2265876.jpeg");
        List<Comentario> comentarios33 = new ArrayList<>();
        comentarios33.add(new Comentario("Súper divertido", new Date(), 5.0f, servicio33));
        comentarios33.add(new Comentario("Adrenalina pura", new Date(), 4.9f, servicio33));
        comentarios33.add(new Comentario("Los niños lo amaron", new Date(), 5.0f, servicio33));
        comentarios33.add(new Comentario("Piloto experto", new Date(), 4.8f, servicio33));
        comentarios33.add(new Comentario("Nos caímos varias veces", new Date(), 5.0f, servicio33));
        comentarios33.add(new Comentario("Chalecos seguros", new Date(), 4.7f, servicio33));
        comentarios33.add(new Comentario("Duración perfecta", new Date(), 4.6f, servicio33));
        comentarios33.add(new Comentario("Risas garantizadas", new Date(), 5.0f, servicio33));
        comentarios33.add(new Comentario("Buen precio", new Date(), 4.5f, servicio33));
        comentarios33.add(new Comentario("Experiencia memorable", new Date(), 4.9f, servicio33));
        servicio33.setComentarios(comentarios33);
        servicios.add(servicio33);

        // Servicio 34: Servicio de Habitación Premium
        Servicio servicio34 = new Servicio(
                "Servicio de Habitación Premium", "Gastronomía",
                "Servicio de comidas gourmet a la habitación disponible 24/7",
                20.0f, "https://images.pexels.com/photos/4577179/pexels-photo-4577179.jpeg");
        List<Comentario> comentarios34 = new ArrayList<>();
        comentarios34.add(new Comentario("Comida deliciosa", new Date(), 4.9f, servicio34));
        comentarios34.add(new Comentario("Llegó caliente", new Date(), 4.8f, servicio34));
        comentarios34.add(new Comentario("Servicio rápido", new Date(), 4.7f, servicio34));
        comentarios34.add(new Comentario("Presentación impecable", new Date(), 4.9f, servicio34));
        comentarios34.add(new Comentario("Menú variado", new Date(), 4.6f, servicio34));
        comentarios34.add(new Comentario("Personal muy educado", new Date(), 5.0f, servicio34));
        comentarios34.add(new Comentario("Disponible toda la noche", new Date(), 4.8f, servicio34));
        comentarios34.add(new Comentario("Porciones generosas", new Date(), 4.7f, servicio34));
        comentarios34.add(new Comentario("Precio razonable", new Date(), 4.5f, servicio34));
        comentarios34.add(new Comentario("Muy conveniente", new Date(), 4.8f, servicio34));
        servicio34.setComentarios(comentarios34);
        servicios.add(servicio34);

        // Servicio 35: Temazcal
        Servicio servicio35 = new Servicio(
                "Temazcal", "Spa y Bienestar",
                "Ceremonia ancestral de purificación en temazcal tradicional",
                60.0f, "https://images.pexels.com/photos/3822621/pexels-photo-3822621.jpeg");
        List<Comentario> comentarios35 = new ArrayList<>();
        comentarios35.add(new Comentario("Experiencia espiritual única", new Date(), 5.0f, servicio35));
        comentarios35.add(new Comentario("Muy purificante", new Date(), 4.9f, servicio35));
        comentarios35.add(new Comentario("Guía muy respetuoso", new Date(), 4.8f, servicio35));
        comentarios35.add(new Comentario("Ceremonial auténtico", new Date(), 5.0f, servicio35));
        comentarios35.add(new Comentario("Me sentí renovado", new Date(), 4.9f, servicio35));
        comentarios35.add(new Comentario("Intenso pero gratificante", new Date(), 4.7f, servicio35));
        comentarios35.add(new Comentario("Bebidas refrescantes al final", new Date(), 4.8f, servicio35));
        comentarios35.add(new Comentario("Conexión con la naturaleza", new Date(), 5.0f, servicio35));
        comentarios35.add(new Comentario("Totalmente recomendado", new Date(), 4.9f, servicio35));
        comentarios35.add(new Comentario("Eliminé muchas toxinas", new Date(), 4.8f, servicio35));
        servicio35.setComentarios(comentarios35);
        servicios.add(servicio35);

        // Servicio 36: Voleibol Playero
        Servicio servicio36 = new Servicio(
                "Voleibol Playero", "Deportes",
                "Torneos y juegos recreativos de voleibol en la playa",
                10.0f, "https://images.pexels.com/photos/1263348/pexels-photo-1263348.jpeg");
        List<Comentario> comentarios36 = new ArrayList<>();
        comentarios36.add(new Comentario("Muy divertido", new Date(), 4.8f, servicio36));
        comentarios36.add(new Comentario("Conocimos gente nueva", new Date(), 4.7f, servicio36));
        comentarios36.add(new Comentario("Organizadores geniales", new Date(), 4.9f, servicio36));
        comentarios36.add(new Comentario("Buen ejercicio", new Date(), 4.6f, servicio36));
        comentarios36.add(new Comentario("Competencia sana", new Date(), 4.7f, servicio36));
        comentarios36.add(new Comentario("Arena perfecta", new Date(), 4.5f, servicio36));
        comentarios36.add(new Comentario("Torneos todas las tardes", new Date(), 4.8f, servicio36));
        comentarios36.add(new Comentario("Para todos los niveles", new Date(), 4.6f, servicio36));
        comentarios36.add(new Comentario("Ambiente alegre", new Date(), 4.7f, servicio36));
        comentarios36.add(new Comentario("Precio simbólico", new Date(), 4.9f, servicio36));
        servicio36.setComentarios(comentarios36);
        servicios.add(servicio36);

        // Servicio 37: Cena Temática Internacional
        Servicio servicio37 = new Servicio(
                "Cena Temática Internacional", "Gastronomía",
                "Cenas temáticas semanales con cocina de diferentes países",
                65.0f, "https://images.pexels.com/photos/1267320/pexels-photo-1267320.jpeg");
        List<Comentario> comentarios37 = new ArrayList<>();
        comentarios37.add(new Comentario("Comida auténtica", new Date(), 5.0f, servicio37));
        comentarios37.add(new Comentario("Chef muy talentoso", new Date(), 4.9f, servicio37));
        comentarios37.add(new Comentario("Decoración temática increíble", new Date(), 4.8f, servicio37));
        comentarios37.add(new Comentario("Gran variedad", new Date(), 4.7f, servicio37));
        comentarios37.add(new Comentario("Música en vivo acorde al tema", new Date(), 4.9f, servicio37));
        comentarios37.add(new Comentario("Porciones generosas", new Date(), 4.8f, servicio37));
        comentarios37.add(new Comentario("Cada semana un país diferente", new Date(), 5.0f, servicio37));
        comentarios37.add(new Comentario("Servicio excelente", new Date(), 4.8f, servicio37));
        comentarios37.add(new Comentario("Incluye bebida típica", new Date(), 4.7f, servicio37));
        comentarios37.add(new Comentario("Experiencia cultural", new Date(), 4.9f, servicio37));
        servicio37.setComentarios(comentarios37);
        servicios.add(servicio37);

        // Servicio 38: Flyboard
        Servicio servicio38 = new Servicio(
                "Flyboard", "Deportes Acuáticos",
                "Vuela sobre el agua con la última tecnología en deportes acuáticos",
                110.0f, "https://images.pexels.com/photos/1654698/pexels-photo-1654698.jpeg");
        List<Comentario> comentarios38 = new ArrayList<>();
        comentarios38.add(new Comentario("Experiencia alucinante", new Date(), 5.0f, servicio38));
        comentarios38.add(new Comentario("Adrenalina al máximo", new Date(), 5.0f, servicio38));
        comentarios38.add(new Comentario("Instructor muy profesional", new Date(), 4.9f, servicio38));
        comentarios38.add(new Comentario("Logré volar al tercer intento", new Date(), 4.8f, servicio38));
        comentarios38.add(new Comentario("Equipo de seguridad top", new Date(), 5.0f, servicio38));
        comentarios38.add(new Comentario("Vale cada centavo", new Date(), 4.9f, servicio38));
        comentarios38.add(new Comentario("Sensación increíble", new Date(), 5.0f, servicio38));
        comentarios38.add(new Comentario("Más difícil de lo que parece", new Date(), 4.7f, servicio38));
        comentarios38.add(new Comentario("Fotos y video incluidos", new Date(), 4.8f, servicio38));
        comentarios38.add(new Comentario("Lo mejor que he hecho", new Date(), 5.0f, servicio38));
        servicio38.setComentarios(comentarios38);
        servicios.add(servicio38);

        // Servicio 39: Meditación Guiada
        Servicio servicio39 = new Servicio(
                "Meditación Guiada", "Bienestar",
                "Sesiones de meditación al aire libre con maestro certificado",
                20.0f, "https://images.pexels.com/photos/3822864/pexels-photo-3822864.jpeg");
        List<Comentario> comentarios39 = new ArrayList<>();
        comentarios39.add(new Comentario("Paz interior", new Date(), 5.0f, servicio39));
        comentarios39.add(new Comentario("Maestro muy sabio", new Date(), 4.9f, servicio39));
        comentarios39.add(new Comentario("Lugar perfecto en la playa", new Date(), 5.0f, servicio39));
        comentarios39.add(new Comentario("Me ayudó a desconectar", new Date(), 4.8f, servicio39));
        comentarios39.add(new Comentario("Técnicas fáciles de seguir", new Date(), 4.7f, servicio39));
        comentarios39.add(new Comentario("Sonido de las olas relajante", new Date(), 5.0f, servicio39));
        comentarios39.add(new Comentario("Todos los días a las 7am", new Date(), 4.8f, servicio39));
        comentarios39.add(new Comentario("Duración ideal", new Date(), 4.6f, servicio39));
        comentarios39.add(new Comentario("Totalmente recomendado", new Date(), 4.9f, servicio39));
        comentarios39.add(new Comentario("Precio muy accesible", new Date(), 5.0f, servicio39));
        servicio39.setComentarios(comentarios39);
        servicios.add(servicio39);

        // Servicio 40: Bar de Jugos y Smoothies
        Servicio servicio40 = new Servicio(
                "Bar de Jugos y Smoothies", "Bebidas Saludables",
                "Bar de jugos naturales y smoothies con frutas tropicales frescas",
                12.0f, "https://images.pexels.com/photos/1092730/pexels-photo-1092730.jpeg");
        List<Comentario> comentarios40 = new ArrayList<>();
        comentarios40.add(new Comentario("Jugos deliciosos", new Date(), 4.9f, servicio40));
        comentarios40.add(new Comentario("Frutas super frescas", new Date(), 5.0f, servicio40));
        comentarios40.add(new Comentario("Gran variedad", new Date(), 4.8f, servicio40));
        comentarios40.add(new Comentario("Perfecto para el calor", new Date(), 4.7f, servicio40));
        comentarios40.add(new Comentario("Smoothies cremosos", new Date(), 4.9f, servicio40));
        comentarios40.add(new Comentario("Pueden agregar proteína", new Date(), 4.6f, servicio40));
        comentarios40.add(new Comentario("Servicio rápido", new Date(), 4.5f, servicio40));
        comentarios40.add(new Comentario("Precios razonables", new Date(), 4.7f, servicio40));
        comentarios40.add(new Comentario("Opciones veganas", new Date(), 4.8f, servicio40));
        comentarios40.add(new Comentario("Refrescante y saludable", new Date(), 4.9f, servicio40));
        servicio40.setComentarios(comentarios40);
        servicios.add(servicio40);

        // Servicio 41: Masaje Prenatal
        Servicio servicio41 = new Servicio(
                "Masaje Prenatal", "Spa y Bienestar",
                "Masaje especializado para futuras mamás con técnicas seguras",
                90.0f, "https://images.pexels.com/photos/3997987/pexels-photo-3997987.jpeg");
        List<Comentario> comentarios41 = new ArrayList<>();
        comentarios41.add(new Comentario("Alivió mi dolor de espalda", new Date(), 5.0f, servicio41));
        comentarios41.add(new Comentario("Terapeuta especializada", new Date(), 5.0f, servicio41));
        comentarios41.add(new Comentario("Camilla especial muy cómoda", new Date(), 4.9f, servicio41));
        comentarios41.add(new Comentario("Técnica segura y relajante", new Date(), 5.0f, servicio41));
        comentarios41.add(new Comentario("Me sentí cuidada", new Date(), 4.9f, servicio41));
        comentarios41.add(new Comentario("Presión perfecta", new Date(), 4.8f, servicio41));
        comentarios41.add(new Comentario("Aceites seguros para el embarazo", new Date(), 5.0f, servicio41));
        comentarios41.add(new Comentario("Duración adecuada", new Date(), 4.7f, servicio41));
        comentarios41.add(new Comentario("Totalmente recomendado", new Date(), 5.0f, servicio41));
        comentarios41.add(new Comentario("Vale la pena", new Date(), 4.9f, servicio41));
        servicio41.setComentarios(comentarios41);
        servicios.add(servicio41);

        // Servicio 42: Aqua Zumba
        Servicio servicio42 = new Servicio(
                "Aqua Zumba", "Fitness",
                "Clases de zumba en la piscina con ritmos latinos",
                15.0f, "https://images.pexels.com/photos/863988/pexels-photo-863988.jpeg");
        List<Comentario> comentarios42 = new ArrayList<>();
        comentarios42.add(new Comentario("Súper divertido", new Date(), 5.0f, servicio42));
        comentarios42.add(new Comentario("Instructora con mucha energía", new Date(), 4.9f, servicio42));
        comentarios42.add(new Comentario("Buen ejercicio sin sudar", new Date(), 4.8f, servicio42));
        comentarios42.add(new Comentario("Música alegre", new Date(), 4.7f, servicio42));
        comentarios42.add(new Comentario("Para todos los niveles", new Date(), 4.8f, servicio42));
        comentarios42.add(new Comentario("Conocí gente nueva", new Date(), 4.6f, servicio42));
        comentarios42.add(new Comentario("Clases todos los días", new Date(), 4.7f, servicio42));
        comentarios42.add(new Comentario("Quemas calorías sin darte cuenta", new Date(), 4.9f, servicio42));
        comentarios42.add(new Comentario("Ambiente muy alegre", new Date(), 4.8f, servicio42));
        comentarios42.add(new Comentario("Precio accesible", new Date(), 4.9f, servicio42));
        servicio42.setComentarios(comentarios42);
        servicios.add(servicio42);

        // Servicio 43: Tour de Snorkeling Nocturno
        Servicio servicio43 = new Servicio(
                "Tour de Snorkeling Nocturno", "Deportes Acuáticos",
                "Experiencia única de snorkeling nocturno con linternas submarinas",
                75.0f, "https://images.pexels.com/photos/2404370/pexels-photo-2404370.jpeg");
        List<Comentario> comentarios43 = new ArrayList<>();
        comentarios43.add(new Comentario("Experiencia mágica", new Date(), 5.0f, servicio43));
        comentarios43.add(new Comentario("Vida marina diferente de noche", new Date(), 5.0f, servicio43));
        comentarios43.add(new Comentario("Linternas potentes", new Date(), 4.8f, servicio43));
        comentarios43.add(new Comentario("Guía muy profesional", new Date(), 4.9f, servicio43));
        comentarios43.add(new Comentario("Vimos pulpos y langostas", new Date(), 5.0f, servicio43));
        comentarios43.add(new Comentario("Equipo de seguridad completo", new Date(), 4.8f, servicio43));
        comentarios43.add(new Comentario("Grupos pequeños", new Date(), 4.7f, servicio43));
        comentarios43.add(new Comentario("Inolvidable", new Date(), 5.0f, servicio43));
        comentarios43.add(new Comentario("Bebida caliente al final", new Date(), 4.6f, servicio43));
        comentarios43.add(new Comentario("Totalmente recomendado", new Date(), 5.0f, servicio43));
        servicio43.setComentarios(comentarios43);
        servicios.add(servicio43);

        // Servicio 44: Sauna y Baño Turco
        Servicio servicio44 = new Servicio(
                "Sauna y Baño Turco", "Spa y Bienestar",
                "Acceso ilimitado a sauna finlandesa y baño turco",
                30.0f, "https://images.pexels.com/photos/3865554/pexels-photo-3865554.jpeg");
        List<Comentario> comentarios44 = new ArrayList<>();
        comentarios44.add(new Comentario("Muy relajante", new Date(), 4.9f, servicio44));
        comentarios44.add(new Comentario("Instalaciones limpias", new Date(), 5.0f, servicio44));
        comentarios44.add(new Comentario("Temperatura perfecta", new Date(), 4.8f, servicio44));
        comentarios44.add(new Comentario("Purificante", new Date(), 4.7f, servicio44));
        comentarios44.add(new Comentario("Duchas frías disponibles", new Date(), 4.8f, servicio44));
        comentarios44.add(new Comentario("Área tranquila", new Date(), 4.9f, servicio44));
        comentarios44.add(new Comentario("Toallas incluidas", new Date(), 4.6f, servicio44));
        comentarios44.add(new Comentario("Buen precio", new Date(), 4.7f, servicio44));
        comentarios44.add(new Comentario("Perfecto después del gimnasio", new Date(), 4.8f, servicio44));
        comentarios44.add(new Comentario("Lo usé todos los días", new Date(), 4.9f, servicio44));
        servicio44.setComentarios(comentarios44);
        servicios.add(servicio44);

        // Servicio 45: Parrillada en la Playa
        Servicio servicio45 = new Servicio(
                "Parrillada en la Playa", "Gastronomía",
                "BBQ nocturno en la playa con carnes, mariscos y música en vivo",
                80.0f, "https://images.pexels.com/photos/1105325/pexels-photo-1105325.jpeg");
        List<Comentario> comentarios45 = new ArrayList<>();
        comentarios45.add(new Comentario("Comida deliciosa", new Date(), 5.0f, servicio45));
        comentarios45.add(new Comentario("Carnes en su punto", new Date(), 4.9f, servicio45));
        comentarios45.add(new Comentario("Mariscos fresquísimos", new Date(), 5.0f, servicio45));
        comentarios45.add(new Comentario("Ambiente increíble", new Date(), 4.8f, servicio45));
        comentarios45.add(new Comentario("Música en vivo genial", new Date(), 4.9f, servicio45));
        comentarios45.add(new Comentario("Fogata y antorchas", new Date(), 5.0f, servicio45));
        comentarios45.add(new Comentario("Bebidas ilimitadas", new Date(), 4.8f, servicio45));
        comentarios45.add(new Comentario("Servicio atento", new Date(), 4.7f, servicio45));
        comentarios45.add(new Comentario("Postres deliciosos", new Date(), 4.8f, servicio45));
        comentarios45.add(new Comentario("Noche memorable", new Date(), 5.0f, servicio45));
        servicio45.setComentarios(comentarios45);
        servicios.add(servicio45);

        // Servicio 46: Clases de Mixología
        Servicio servicio46 = new Servicio(
                "Clases de Mixología", "Entretenimiento",
                "Aprende a preparar cocteles tropicales con bartender profesional",
                40.0f, "https://images.pexels.com/photos/5490965/pexels-photo-5490965.jpeg");
        List<Comentario> comentarios46 = new ArrayList<>();
        comentarios46.add(new Comentario("Muy divertido", new Date(), 5.0f, servicio46));
        comentarios46.add(new Comentario("Aprendí muchas recetas", new Date(), 4.8f, servicio46));
        comentarios46.add(new Comentario("Bartender muy didáctico", new Date(), 4.9f, servicio46));
        comentarios46.add(new Comentario("Probamos lo que hicimos", new Date(), 5.0f, servicio46));
        comentarios46.add(new Comentario("Técnicas profesionales", new Date(), 4.7f, servicio46));
        comentarios46.add(new Comentario("Grupo pequeño", new Date(), 4.8f, servicio46));
        comentarios46.add(new Comentario("Recetas para llevar", new Date(), 4.6f, servicio46));
        comentarios46.add(new Comentario("Ingredientes de calidad", new Date(), 4.9f, servicio46));
        comentarios46.add(new Comentario("Ambiente relajado", new Date(), 4.7f, servicio46));
        comentarios46.add(new Comentario("Vale la pena", new Date(), 4.8f, servicio46));
        servicio46.setComentarios(comentarios46);
        servicios.add(servicio46);

        // Servicio 47: Masaje en Pareja
        Servicio servicio47 = new Servicio(
                "Masaje en Pareja", "Spa y Bienestar",
                "Sesión de masaje simultáneo para parejas en sala privada",
                170.0f, "https://images.pexels.com/photos/3757959/pexels-photo-3757959.jpeg");
        List<Comentario> comentarios47 = new ArrayList<>();
        comentarios47.add(new Comentario("Experiencia romántica", new Date(), 5.0f, servicio47));
        comentarios47.add(new Comentario("Sala hermosa con vista", new Date(), 5.0f, servicio47));
        comentarios47.add(new Comentario("Ambos terapeutas excelentes", new Date(), 4.9f, servicio47));
        comentarios47.add(new Comentario("Champán de cortesía", new Date(), 4.8f, servicio47));
        comentarios47.add(new Comentario("Música relajante", new Date(), 4.7f, servicio47));
        comentarios47.add(new Comentario("Privacidad total", new Date(), 5.0f, servicio47));
        comentarios47.add(new Comentario("Pétalos de rosa", new Date(), 4.9f, servicio47));
        comentarios47.add(new Comentario("Técnica sincronizada", new Date(), 4.8f, servicio47));
        comentarios47.add(new Comentario("Perfecto para aniversario", new Date(), 5.0f, servicio47));
        comentarios47.add(new Comentario("Vale cada centavo", new Date(), 4.9f, servicio47));
        servicio47.setComentarios(comentarios47);
        servicios.add(servicio47);

        // Servicio 48: Bodyboard
        Servicio servicio48 = new Servicio(
                "Bodyboard", "Deportes Acuáticos",
                "Alquiler de bodyboards y clases para surfear las olas",
                25.0f, "https://images.pexels.com/photos/416676/pexels-photo-416676.jpeg");
        List<Comentario> comentarios48 = new ArrayList<>();
        comentarios48.add(new Comentario("Muy divertido", new Date(), 4.8f, servicio48));
        comentarios48.add(new Comentario("Más fácil que el surf", new Date(), 4.7f, servicio48));
        comentarios48.add(new Comentario("Tablas en buen estado", new Date(), 4.6f, servicio48));
        comentarios48.add(new Comentario("Instructor paciente", new Date(), 4.8f, servicio48));
        comentarios48.add(new Comentario("Olas perfectas", new Date(), 4.9f, servicio48));
        comentarios48.add(new Comentario("Para niños y adultos", new Date(), 4.7f, servicio48));
        comentarios48.add(new Comentario("Aletas incluidas", new Date(), 4.5f, servicio48));
        comentarios48.add(new Comentario("Buen ejercicio", new Date(), 4.6f, servicio48));
        comentarios48.add(new Comentario("Precio accesible", new Date(), 4.8f, servicio48));
        comentarios48.add(new Comentario("Lo repetiría", new Date(), 4.7f, servicio48));
        servicio48.setComentarios(comentarios48);
        servicios.add(servicio48);

        // Servicio 49: Conciertos en Vivo
        Servicio servicio49 = new Servicio(
                "Conciertos en Vivo", "Entretenimiento",
                "Shows musicales en vivo con artistas locales e internacionales",
                50.0f, "https://images.pexels.com/photos/1105666/pexels-photo-1105666.jpeg");
        List<Comentario> comentarios49 = new ArrayList<>();
        comentarios49.add(new Comentario("Artistas increíbles", new Date(), 5.0f, servicio49));
        comentarios49.add(new Comentario("Variedad de géneros musicales", new Date(), 4.8f, servicio49));
        comentarios49.add(new Comentario("Buena acústica", new Date(), 4.7f, servicio49));
        comentarios49.add(new Comentario("Escenario al aire libre", new Date(), 4.9f, servicio49));
        comentarios49.add(new Comentario("Bebidas durante el show", new Date(), 4.6f, servicio49));
        comentarios49.add(new Comentario("Shows todos los fines de semana", new Date(), 4.8f, servicio49));
        comentarios49.add(new Comentario("Ambiente espectacular", new Date(), 5.0f, servicio49));
        comentarios49.add(new Comentario("Asientos cómodos", new Date(), 4.5f, servicio49));
        comentarios49.add(new Comentario("Pista de baile", new Date(), 4.7f, servicio49));
        comentarios49.add(new Comentario("Noche perfecta", new Date(), 4.9f, servicio49));
        servicio49.setComentarios(comentarios49);
        servicios.add(servicio49);

        // Servicio 50: Excursión a Islas Cercanas
        Servicio servicio50 = new Servicio(
                "Excursión a Islas Cercanas", "Ecoturismo",
                "Tour de día completo a islas paradisíacas con almuerzo incluido",
                130.0f, "https://images.pexels.com/photos/1450353/pexels-photo-1450353.jpeg");
        List<Comentario> comentarios50 = new ArrayList<>();
        comentarios50.add(new Comentario("Día perfecto", new Date(), 5.0f, servicio50));
        comentarios50.add(new Comentario("Islas espectaculares", new Date(), 5.0f, servicio50));
        comentarios50.add(new Comentario("Guía con mucha información", new Date(), 4.9f, servicio50));
        comentarios50.add(new Comentario("Almuerzo delicioso", new Date(), 4.8f, servicio50));
        comentarios50.add(new Comentario("Tiempo suficiente en cada isla", new Date(), 4.7f, servicio50));
        comentarios50.add(new Comentario("Barco cómodo", new Date(), 4.8f, servicio50));
        comentarios50.add(new Comentario("Snorkel incluido", new Date(), 4.9f, servicio50));
        comentarios50.add(new Comentario("Playas vírgenes", new Date(), 5.0f, servicio50));
        comentarios50.add(new Comentario("Bebidas incluidas", new Date(), 4.6f, servicio50));
        comentarios50.add(new Comentario("Lo mejor del viaje", new Date(), 5.0f, servicio50));
        servicio50.setComentarios(comentarios50);
        servicios.add(servicio50);

        // Guardar todos los servicios
        servicioRepository.saveAll(servicios);

        // ============================
        //  CREAR 20 RESERVAS
        // ============================
        List<Reserva> reservas = new ArrayList<>();
        String[] estadosReserva = {"CONFIRMADA", "PENDIENTE", "CANCELADA"};

        // Obtener usuarios y habitaciones guardadas
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<Habitacion> habitacionesGuardadas = habitacionRepository.findAll();

        // Reserva 1
        reservas.add(new Reserva(
                new Date(2024 - 1900, 0, 15),
                new Date(2024 - 1900, 0, 20),
                2,
                "CONFIRMADA",
                usuarios.get(0),
                habitacionesGuardadas.get(10)
        ));

        // Reserva 2
        reservas.add(new Reserva(
                new Date(2024 - 1900, 0, 22),
                new Date(2024 - 1900, 0, 28),
                4,
                "CONFIRMADA",
                usuarios.get(1),
                habitacionesGuardadas.get(15)
        ));

        // Reserva 3
        reservas.add(new Reserva(
                new Date(2024 - 1900, 1, 5),
                new Date(2024 - 1900, 1, 12),
                2,
                "PENDIENTE",
                usuarios.get(2),
                habitacionesGuardadas.get(20)
        ));

        // Reserva 4
        reservas.add(new Reserva(
                new Date(2024 - 1900, 1, 14),
                new Date(2024 - 1900, 1, 21),
                3,
                "CONFIRMADA",
                usuarios.get(3),
                habitacionesGuardadas.get(25)
        ));

        // Reserva 5
        reservas.add(new Reserva(
                new Date(2024 - 1900, 2, 1),
                new Date(2024 - 1900, 2, 7),
                2,
                "CANCELADA",
                usuarios.get(4),
                habitacionesGuardadas.get(30)
        ));

        // Reserva 6
        reservas.add(new Reserva(
                new Date(2024 - 1900, 2, 10),
                new Date(2024 - 1900, 2, 17),
                4,
                "CONFIRMADA",
                usuarios.get(5),
                habitacionesGuardadas.get(35)
        ));

        // Reserva 7
        reservas.add(new Reserva(
                new Date(2024 - 1900, 3, 5),
                new Date(2024 - 1900, 3, 12),
                2,
                "CONFIRMADA",
                usuarios.get(6),
                habitacionesGuardadas.get(40)
        ));

        // Reserva 8
        reservas.add(new Reserva(
                new Date(2024 - 1900, 3, 15),
                new Date(2024 - 1900, 3, 22),
                3,
                "PENDIENTE",
                usuarios.get(7),
                habitacionesGuardadas.get(45)
        ));

        // Reserva 9
        reservas.add(new Reserva(
                new Date(2024 - 1900, 4, 1),
                new Date(2024 - 1900, 4, 8),
                2,
                "CONFIRMADA",
                usuarios.get(8),
                habitacionesGuardadas.get(50)
        ));

        // Reserva 10
        reservas.add(new Reserva(
                new Date(2024 - 1900, 4, 10),
                new Date(2024 - 1900, 4, 17),
                4,
                "CONFIRMADA",
                usuarios.get(9),
                habitacionesGuardadas.get(55)
        ));

        // Reserva 11
        reservas.add(new Reserva(
                new Date(2024 - 1900, 5, 5),
                new Date(2024 - 1900, 5, 12),
                2,
                "CANCELADA",
                usuarios.get(0),
                habitacionesGuardadas.get(60)
        ));

        // Reserva 12
        reservas.add(new Reserva(
                new Date(2024 - 1900, 5, 20),
                new Date(2024 - 1900, 5, 27),
                3,
                "CONFIRMADA",
                usuarios.get(1),
                habitacionesGuardadas.get(65)
        ));

        // Reserva 13
        reservas.add(new Reserva(
                new Date(2024 - 1900, 6, 1),
                new Date(2024 - 1900, 6, 8),
                2,
                "PENDIENTE",
                usuarios.get(2),
                habitacionesGuardadas.get(70)
        ));

        // Reserva 14
        reservas.add(new Reserva(
                new Date(2024 - 1900, 6, 15),
                new Date(2024 - 1900, 6, 22),
                4,
                "CONFIRMADA",
                usuarios.get(3),
                habitacionesGuardadas.get(75)
        ));

        // Reserva 15
        reservas.add(new Reserva(
                new Date(2024 - 1900, 7, 5),
                new Date(2024 - 1900, 7, 12),
                2,
                "CONFIRMADA",
                usuarios.get(4),
                habitacionesGuardadas.get(80)
        ));

        // Reserva 16
        reservas.add(new Reserva(
                new Date(2024 - 1900, 7, 20),
                new Date(2024 - 1900, 7, 27),
                3,
                "CONFIRMADA",
                usuarios.get(5),
                habitacionesGuardadas.get(85)
        ));

        // Reserva 17
        reservas.add(new Reserva(
                new Date(2024 - 1900, 8, 1),
                new Date(2024 - 1900, 8, 8),
                2,
                "PENDIENTE",
                usuarios.get(6),
                habitacionesGuardadas.get(90)
        ));

        // Reserva 18
        reservas.add(new Reserva(
                new Date(2024 - 1900, 8, 15),
                new Date(2024 - 1900, 8, 22),
                4,
                "CONFIRMADA",
                usuarios.get(7),
                habitacionesGuardadas.get(95)
        ));

        // Reserva 19
        reservas.add(new Reserva(
                new Date(2024 - 1900, 9, 5),
                new Date(2024 - 1900, 9, 12),
                2,
                "CANCELADA",
                usuarios.get(8),
                habitacionesGuardadas.get(5)
        ));

        // Reserva 20
        reservas.add(new Reserva(
                new Date(2024 - 1900, 9, 20),
                new Date(2024 - 1900, 9, 27),
                3,
                "CONFIRMADA",
                usuarios.get(9),
                habitacionesGuardadas.get(8)
        ));

        reservaRepository.saveAll(reservas);

        System.out.println("==============================================");
        System.out.println("Inicialización de base de datos completada:");
        System.out.println("- 10 Tipos de Habitación");
        System.out.println("- 100 Habitaciones");
        System.out.println("- 50 Servicios con 10 comentarios cada uno");
        System.out.println("- 20 Reservas");
        System.out.println("- 10 Usuarios Clientes");
        System.out.println("- 5 Usuarios Operadores");
        System.out.println("- 5 Usuarios Administradores");
        System.out.println("==============================================");
    }
}
