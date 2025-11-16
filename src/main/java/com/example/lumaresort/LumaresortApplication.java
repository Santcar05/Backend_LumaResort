package com.example.lumaresort;

import java.util.ArrayList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

import com.example.lumaresort.entities.Usuario;

@EntityScan(basePackages = "com.example.lumaresort.entities")
@SpringBootApplication
public class LumaresortApplication {

    public static void main(String[] args) {
        SpringApplication.run(LumaresortApplication.class, args);
    }

    //Crear bean
    @Bean
    public Usuario usuario() {
        return Usuario.builder()
                .nombre("")
                .apellido("")
                .correo("")
                .contrasena("")
                .cedula("")
                .telefono("")
                .roles(new ArrayList<>())
                .build();
    }

    @Bean
    public Usuario usuarioCrearCuenta() {
        return Usuario.builder()
                .nombre("")
                .apellido("")
                .correo("")
                .contrasena("")
                .cedula("")
                .telefono("")
                .roles(new ArrayList<>())
                .build();
    }

}

//Proceso para que a veces funcione si dice que el puerto ya esta en uso
// Ir a la terminal: tasklist | findstr java
// Matar todos los proceso java: taskkill /F /PID 19420 
