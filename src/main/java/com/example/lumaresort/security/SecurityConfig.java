package com.example.lumaresort.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                //  ENDPOINTS PÚBLICOS 
                .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
                .requestMatchers("/h2/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/actividades").permitAll()
                .requestMatchers("/actividades/**").permitAll()
                // Chatbot de contacto
                .requestMatchers("/api/chatbot/**").permitAll()
                .requestMatchers("/api/chatbot").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/chatbot/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/chatbot/**").permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/api/chatbot/**").permitAll()
                // ========== CLIMA ==========
                .requestMatchers("/api/clima/**").permitAll()
                .requestMatchers("/api/clima").permitAll()
                // ========== SERVIR ARCHIVOS SUBIDOS - PÚBLICO ==========
                .requestMatchers("/uploads/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/uploads/**").permitAll()
                //ENDPOINTS DE AUTENTICACIÓN 
                .requestMatchers(HttpMethod.GET, "/api/auth/actual").authenticated()
                .requestMatchers(HttpMethod.PUT, "/api/auth/perfil").authenticated()
                // ENDPOINTS EXCLUSIVOS DE ADMINISTRADOR 
                .requestMatchers("/admin/**").hasRole("ADMINISTRADOR")
                .requestMatchers("/clientes/**").hasRole("ADMINISTRADOR")
                // Gestión de habitaciones: solo ADMIN puede crear/modificar/eliminar
                .requestMatchers(HttpMethod.POST, "/habitaciones/**").hasRole("ADMINISTRADOR")
                .requestMatchers(HttpMethod.PUT, "/habitaciones/**").hasRole("ADMINISTRADOR")
                .requestMatchers(HttpMethod.DELETE, "/habitaciones/**").hasRole("ADMINISTRADOR")
                // Ver habitaciones y disponibilidad: público (para que clientes puedan reservar)
                .requestMatchers(HttpMethod.GET, "/habitaciones/disponibles").permitAll()
                .requestMatchers(HttpMethod.GET, "/habitaciones/**").permitAll()
                // Gestión de tipos de habitación: solo ADMIN puede crear/modificar/eliminar
                .requestMatchers(HttpMethod.POST, "/tipoHabitacion/**").hasRole("ADMINISTRADOR")
                .requestMatchers(HttpMethod.POST, "/tiposHabitacion/**").hasRole("ADMINISTRADOR")
                .requestMatchers(HttpMethod.PUT, "/tipoHabitacion/**").hasRole("ADMINISTRADOR")
                .requestMatchers(HttpMethod.PUT, "/tiposHabitacion/**").hasRole("ADMINISTRADOR")
                .requestMatchers(HttpMethod.DELETE, "/tipoHabitacion/**").hasRole("ADMINISTRADOR")
                .requestMatchers(HttpMethod.DELETE, "/tiposHabitacion/**").hasRole("ADMINISTRADOR")
                // Ver tipos de habitación: público
                .requestMatchers(HttpMethod.GET, "/tipoHabitacion/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/tiposHabitacion/**").permitAll()
                // ========== ENDPOINTS EXCLUSIVOS DE OPERADOR ==========
                .requestMatchers("/operador/**").hasRole("OPERADOR")
                // Gestión de pagos (solo OPERADOR)
                .requestMatchers("/pagos/**").hasRole("OPERADOR")
                // Gestión de servicios: POST/PUT/DELETE solo OPERADOR, GET público
                .requestMatchers(HttpMethod.POST, "/servicios/**").hasAnyRole("OPERADOR", "ADMINISTRADOR")
                .requestMatchers(HttpMethod.PUT, "/servicios/**").hasAnyRole("OPERADOR", "ADMINISTRADOR")
                .requestMatchers(HttpMethod.DELETE, "/servicios/**").hasAnyRole("OPERADOR", "ADMINISTRADOR")
                .requestMatchers(HttpMethod.GET, "/servicios/**").permitAll()
                // ========== ENDPOINTS DE RESERVAS ==========
                // Ver TODAS las reservas (OPERADOR y ADMINISTRADOR)
                .requestMatchers(HttpMethod.GET, "/reservas").hasAnyRole("OPERADOR", "ADMINISTRADOR")
                // Crear nueva reserva (cualquier usuario autenticado)
                .requestMatchers(HttpMethod.POST, "/reservas").authenticated()
                // Buscar mis reservas (cualquier usuario autenticado)
                .requestMatchers(HttpMethod.GET, "/reservas/buscar/**").authenticated()
                // Ver detalles de una reserva, modificar, cancelar (cualquier autenticado)
                .requestMatchers(HttpMethod.GET, "/reservas/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/reservas/**").authenticated()
                .requestMatchers(HttpMethod.PUT, "/reservas/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/reservas/**").authenticated()
                // Ver el usuario actual si está autenticado
                .requestMatchers(HttpMethod.GET, "/usuario").hasAnyRole("OPERADOR", "ADMINISTRADOR")
                .requestMatchers(HttpMethod.GET, "/usuario/**").hasAnyRole("OPERADOR", "ADMINISTRADOR")
                // Cualquier otra solicitud requiere autenticación
                .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // Para H2 Console
        http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200", "http://localhost:3000", "http://localhost:3000", "https://jazzy-custard-700f04.netlify.app"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
