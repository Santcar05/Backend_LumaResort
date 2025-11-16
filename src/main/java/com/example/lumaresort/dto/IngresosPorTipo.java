package com.example.lumaresort.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngresosPorTipo {

    private Map<String, Double> ingresosPorTipo;
}
