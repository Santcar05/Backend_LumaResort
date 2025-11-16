package com.example.lumaresort.controller;

import com.example.lumaresort.dto.AuthResponse;
import com.example.lumaresort.dto.LoginRequest;
import com.example.lumaresort.dto.RegisterRequest;
import com.example.lumaresort.dto.UpdateProfileRequest;
import com.example.lumaresort.dto.UserResponse;
import com.example.lumaresort.entities.Cliente;
import com.example.lumaresort.entities.ERole;
import com.example.lumaresort.entities.Role;
import com.example.lumaresort.entities.Usuario;
import com.example.lumaresort.repository.ClienteRepository;
import com.example.lumaresort.repository.RoleRepository;
import com.example.lumaresort.repository.UsuarioRepository;
import com.example.lumaresort.security.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * Endpoint de login
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Autenticar al usuario
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getCorreo(),
                            loginRequest.getContrasena()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generar token JWT
            String jwt = jwtUtils.generateJwtToken(authentication);

            // Obtener información del usuario
            Usuario usuario = usuarioRepository.findByCorreo(loginRequest.getCorreo());

            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("mensaje", "Usuario no encontrado"));
            }

            // Crear respuesta con usuario y token
            UserResponse userResponse = new UserResponse(
                    usuario.getIdUsuario(),
                    usuario.getNombre(),
                    usuario.getApellido(),
                    usuario.getCorreo(),
                    usuario.getCedula(),
                    usuario.getTelefono(),
                    usuario.getRoles().stream()
                            .map(role -> role.getNombre())
                            .collect(Collectors.toList())
            );

            AuthResponse authResponse = new AuthResponse(jwt, userResponse);

            return ResponseEntity.ok(authResponse);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Credenciales inválidas");
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    /**
     * Endpoint de registro (por defecto crea clientes)
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            // Verificar si el correo ya existe
            if (usuarioRepository.findByCorreo(registerRequest.getCorreo()) != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("mensaje", "El correo ya está registrado"));
            }

            // Crear usuario
            Usuario usuario = Usuario.builder()
                    .nombre(registerRequest.getNombre())
                    .apellido(registerRequest.getApellido())
                    .correo(registerRequest.getCorreo())
                    .contrasena(passwordEncoder.encode(registerRequest.getContrasena()))
                    .cedula(registerRequest.getCedula())
                    .telefono(registerRequest.getTelefono())
                    .build();

            // Asignar rol de CLIENTE por defecto
            Role roleCliente = roleRepository.findByNombre(ERole.ROLE_CLIENTE)
                    .orElseGet(() -> roleRepository.save(new Role(ERole.ROLE_CLIENTE)));

            usuario.setRoles(new ArrayList<>(List.of(roleCliente)));

            // Guardar usuario
            Usuario savedUsuario = usuarioRepository.save(usuario);

            // Crear perfil de cliente
            Cliente cliente = new Cliente();
            cliente.setUsuario(savedUsuario);
            clienteRepository.save(cliente);

            // Generar token automáticamente después del registro
            String jwt = jwtUtils.generateTokenFromUsername(savedUsuario.getCorreo());

            // Crear respuesta
            UserResponse userResponse = new UserResponse(
                    savedUsuario.getIdUsuario(),
                    savedUsuario.getNombre(),
                    savedUsuario.getApellido(),
                    savedUsuario.getCorreo(),
                    savedUsuario.getCedula(),
                    savedUsuario.getTelefono(),
                    savedUsuario.getRoles().stream()
                            .map(role -> role.getNombre())
                            .collect(Collectors.toList())
            );

            AuthResponse authResponse = new AuthResponse(jwt, userResponse);

            return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al registrar usuario");
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Endpoint para verificar el token actual
     */
    @GetMapping("/actual")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("mensaje", "No autenticado"));
        }

        String correo = authentication.getName();
        Usuario usuario = usuarioRepository.findByCorreo(correo);

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", "Usuario no encontrado"));
        }

        UserResponse userResponse = new UserResponse(
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getCorreo(),
                usuario.getCedula(),
                usuario.getTelefono(),
                usuario.getRoles().stream()
                        .map(role -> role.getNombre())
                        .collect(Collectors.toList())
        );

        return ResponseEntity.ok(userResponse);
    }

    /**
     * Endpoint para actualizar el perfil del usuario autenticado
     */
    @PutMapping("/perfil")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody UpdateProfileRequest updateRequest,
            Authentication authentication) {
        try {
            // Verificar que el usuario esté autenticado
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("mensaje", "No autenticado"));
            }

            // Obtener el usuario actual por su correo (del token JWT)
            String correo = authentication.getName();
            Usuario usuario = usuarioRepository.findByCorreo(correo);

            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("mensaje", "Usuario no encontrado"));
            }

            // Actualizar solo los campos permitidos (NO correo ni contraseña)
            usuario.setNombre(updateRequest.getNombre());
            usuario.setApellido(updateRequest.getApellido());
            usuario.setCedula(updateRequest.getCedula());
            usuario.setTelefono(updateRequest.getTelefono());

            // Guardar cambios
            Usuario usuarioActualizado = usuarioRepository.save(usuario);

            // Crear respuesta con los datos actualizados
            UserResponse userResponse = new UserResponse(
                    usuarioActualizado.getIdUsuario(),
                    usuarioActualizado.getNombre(),
                    usuarioActualizado.getApellido(),
                    usuarioActualizado.getCorreo(),
                    usuarioActualizado.getCedula(),
                    usuarioActualizado.getTelefono(),
                    usuarioActualizado.getRoles().stream()
                            .map(role -> role.getNombre())
                            .collect(Collectors.toList())
            );

            return ResponseEntity.ok(userResponse);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al actualizar perfil");
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
