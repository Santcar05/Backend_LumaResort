package com.example.lumaresort.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.lumaresort.entities.ERole;
import com.example.lumaresort.entities.Role;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByNombre(ERole nombre);
    Boolean existsByNombre(ERole nombre);
}