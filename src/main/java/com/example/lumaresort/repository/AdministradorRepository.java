package com.example.lumaresort.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.lumaresort.entities.Administrador;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Long> {

}
