package com.example.lumaresort.Repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.lumaresort.entities.ERole;
import com.example.lumaresort.entities.Role;
import com.example.lumaresort.entities.Usuario;
import com.example.lumaresort.repository.RoleRepository;
import com.example.lumaresort.repository.UsuarioRepository;

@DataJpaTest
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void testFindByCorreoAndContrasena() {
        Usuario u = new Usuario();
        u.setCorreo("test@correo.com");
        u.setContrasena("1234");
        usuarioRepository.save(u);

        Usuario encontrado = usuarioRepository.findByCorreoAndContrasena("test@correo.com", "1234");
        assertThat(encontrado).isNotNull();
    }

    @Test
    void testFindByCorreo() {
        Usuario u = new Usuario();
        u.setCorreo("correo@ejemplo.com");
        u.setContrasena("pass");
        usuarioRepository.save(u);

        Usuario encontrado = usuarioRepository.findByCorreo("correo@ejemplo.com");
        assertThat(encontrado.getCorreo()).isEqualTo("correo@ejemplo.com");
    }

    @Test
    void testFindByRoleName() {
        // Crear rol cliente
        Role roleCliente = roleRepository.findByNombre(ERole.ROLE_CLIENTE)
                .orElseGet(() -> roleRepository.save(new Role(ERole.ROLE_CLIENTE)));

        // Crear usuario con rol cliente
        Usuario u = new Usuario();
        u.setCorreo("rol@correo.com");
        u.setContrasena("pass");
        u.setNombre("Cliente");
        u.setApellido("Test");
        u.setRoles(new ArrayList<>(List.of(roleCliente)));
        usuarioRepository.save(u);

        List<Usuario> clientes = usuarioRepository.findByRoleName(ERole.ROLE_CLIENTE);
        assertThat(clientes).isNotEmpty();
    }

    @Test
    void testFindByRoleNameAdministrador() {
        // Crear rol administrador
        Role roleAdmin = roleRepository.findByNombre(ERole.ROLE_ADMINISTRADOR)
                .orElseGet(() -> roleRepository.save(new Role(ERole.ROLE_ADMINISTRADOR)));

        // Crear usuario administrador
        Usuario admin = new Usuario();
        admin.setCorreo("admin@correo.com");
        admin.setContrasena("admin");
        admin.setNombre("Admin");
        admin.setApellido("Test");
        admin.setRoles(new ArrayList<>(List.of(roleAdmin)));
        usuarioRepository.save(admin);

        List<Usuario> admins = usuarioRepository.findByRoleName(ERole.ROLE_ADMINISTRADOR);
        assertThat(admins).isNotEmpty();
        assertThat(admins.get(0).isEsAdministrador()).isTrue();
    }

    @Test
    void testFindByNombreContainingIgnoreCase() {
        Usuario u = new Usuario();
        u.setNombre("Carlos");
        usuarioRepository.save(u);

        List<Usuario> resultado = usuarioRepository.findByNombreContainingIgnoreCase("car");
        assertThat(resultado).isNotEmpty();
    }
}
