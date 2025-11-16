package com.example.lumaresort.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuariosPorMes {

    private List<Integer> usuariosPorMes; // 12 elementos (enero a diciembre)
}
