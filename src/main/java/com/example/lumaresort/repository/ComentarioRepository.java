package com.example.lumaresort.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.lumaresort.entities.Comentario;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

}
