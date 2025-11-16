package com.example.lumaresort.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.lumaresort.entities.ERole;
import com.example.lumaresort.entities.Usuario;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByCorreoAndContrasena(String correo, String contrasena);

    Usuario findByCorreo(String correo);

    List<Usuario> findByNombreContainingIgnoreCase(String nombre);

    // Métodos para buscar usuarios por rol usando la relación many-to-many
    @Query("SELECT u FROM Usuario u JOIN u.roles r WHERE r.nombre = :roleName")
    List<Usuario> findByRoleName(ERole roleName);

}
