package com.example.lumaresort.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "roles")
public class Role {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRole;
    
    @Enumerated(EnumType.STRING)
    private ERole nombre;
    
    public Role(ERole nombre) {
        this.nombre = nombre;
    }
    
    // GETTER para nombre que faltaba
    public ERole getNombre() {
        return nombre;
    }
}