package com.example.lumaresort.entities;

import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    private String nombre;
    private String apellido;
    
    @Column(unique = true, nullable = false)
    private String correo;
    
    private String contrasena;
    private String cedula;
    private String telefono;

    // NUEVA RELACIÓN CON ROLES
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "usuario_roles",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles = new ArrayList<>();

    @JsonIgnore
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Cliente cliente;

    @JsonIgnore
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Administrador administrador;

    @JsonIgnore
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Operador operador;

    @JsonIgnore
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reserva> reservas;

    // MÉTODOS AUXILIARES PARA VERIFICAR ROLES
    public boolean isEsAdministrador() {
        return roles.stream().anyMatch(role -> 
            role.getNombre() == ERole.ROLE_ADMINISTRADOR);
    }

    public boolean isEsOperador() {
        return roles.stream().anyMatch(role -> 
            role.getNombre() == ERole.ROLE_OPERADOR);
    }

    public boolean isEsCliente() {
        return roles.stream().anyMatch(role -> 
            role.getNombre() == ERole.ROLE_CLIENTE);
    }
}