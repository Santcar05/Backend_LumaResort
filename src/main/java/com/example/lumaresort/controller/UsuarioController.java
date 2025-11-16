package com.example.lumaresort.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.lumaresort.entities.Usuario;
import com.example.lumaresort.service.UsuarioService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping()
    public ResponseEntity<List<Usuario>> obtenerUsuarios() {
        List<Usuario> usuarios = usuarioService.findAll();
        return ResponseEntity.ok(usuarios);
    }

    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestBody Map<String, String> credentials) {
        String correo = credentials.get("correo");
        String contrasena = credentials.get("contrasena");

        Usuario usuarioEncontrado = usuarioService.findByCorreoAndContrasena(correo, contrasena);
        if (usuarioEncontrado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(usuarioEncontrado);
    }

    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario) {
        usuario.setIdUsuario(null); // asegurar que sea nuevo
        usuarioService.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

}


/*
 Paso a paso para hacer un CRUD en angular con springboot sin morir en el intento
    1.- Crear la entidad en el backend
    2.- Crear el repositorio en el backend
    3.- Crear el servicio en el backend
    (Verificar que para el caso del post el id es null porque si no no funcionará)
    4.- Crear el controlador en el backend
    (Utilizar RequestBody en en vez de RequestParam)
    (Usar el RestController, CrossOrigin y RequestMapping y tener en cuenta las rutas ya que tienen que ser iguales a la del frontend)
    5.- Crear la entidad en el frontend
    (Usar los mismos nombres de las variables que en el backend)
    6.- Crear el servicio en el frontend
    (Usar HttpClient y Observable para las peticiones, verificar que las rutas sean iguales a las del backend)
    7.- Crear el controlador en el frontend
    (Usar el servicio creado en el paso anterior y crear las funciones para cada operación del CRUD)
    8.- Crear las vistas en el frontend
    (Crear el HTML y el CSS para mostrar los datos y los formularios para crear y actualizar)
    9.- Probar el CRUD
    (Rezar que funcione y si no llamar a Dios)

    #Tips
    - Si no funciona mirar en la consola de google si aparece un error al hacer la petición
    - Si sale un error 400 mirar que las rutas sean iguales en el frontend y backend
    - Si sale un error 500 mirar que el backend esté corriendo y si lo está reiniciarlo por completo (en caso contrario revisar el id null o llamar a Dios de nuevo)
    - Si sale un error CORS mirar que el CrossOrigin tenga la ruta correcta del frontend
    - Si no aparecen los datos en el frontend mirar que el servicio esté bien inyectado en el controlador y que las funciones estén bien llamadas en el HTML
    - Si no se actualizan los datos mirar que el id se esté seteando correctamente en el controlador del frontend
 */
