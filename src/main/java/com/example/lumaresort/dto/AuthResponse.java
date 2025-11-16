package com.example.lumaresort.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String token;
    private String tipo = "Bearer";
    private UserResponse usuario;

    public AuthResponse(String token, UserResponse usuario) {
        this.token = token;
        this.usuario = usuario;
    }
}
