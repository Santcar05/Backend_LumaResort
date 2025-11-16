package com.example.lumaresort.Repository;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.lumaresort.entities.Servicio;
import com.example.lumaresort.repository.ServicioRepository;

@DataJpaTest
public class ServicioRepositoryTest {

    @Autowired
    private ServicioRepository servicioRepository;

    @BeforeEach
    public void setup() {
        Servicio servicio1 = new Servicio(
                "Spa",
                "Spa",
                "Relájate con nuestro servicio de spa completo que incluye sauna, jacuzzi y aromaterapia",
                50.0f,
                "https://images.pexels.com/photos/6621436/pexels-photo-6621436.jpeg"
        );

        Servicio servicio2 = new Servicio(
                "Piscina",
                "Piscina",
                "Relájate en nuestra piscina con vista al mar, disponible las 24 horas",
                50.0f,
                "https://images.pexels.com/photos/1507525/pexels-photo-1507525.jpeg"
        );

        //Agregar servicios a la base de datos
        servicioRepository.save(servicio1);
        servicioRepository.save(servicio2);
    }

    @Test
    public void test_FindAll() {
        //Act
        List<Servicio> servicios = servicioRepository.findAll();

        Assertions.assertEquals(2, servicios.size());
    }

}
