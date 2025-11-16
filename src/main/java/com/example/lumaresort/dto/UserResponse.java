package com.example.lumaresort.dto;

import com.example.lumaresort.entities.ERole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long idUsuario;
    private String nombre;
    private String apellido;
    private String correo;
    private String cedula;
    private String telefono;
    private List<ERole> roles;
    
    public String getTipoUsuario() {
        if (roles.contains(ERole.ROLE_ADMINISTRADOR)) {
            return "ADMINISTRADOR";
        } else if (roles.contains(ERole.ROLE_OPERADOR)) {
            return "OPERADOR";
        } else if (roles.contains(ERole.ROLE_CLIENTE)) {
            return "CLIENTE";
        }
        return "USUARIO";
    }
}