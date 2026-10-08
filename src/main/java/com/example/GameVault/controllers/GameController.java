package com.example.GameVault.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.GameVault.models.Juego;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class GameController {
    private static List<Juego> juegosDb = new ArrayList<>();
    private static long idCounter = 1;

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    static {
        //juegosDb.add(new Juego(idCounter++, "The legend of Zelda", "Aventura épica.", "zelda.jpg"));
    }

    @GetMapping("/fragments-demo")
    public String fragmentDemo() {
        return "fragments-demo";
    }

    @GetMapping("/juegos")
    public String listarJuegos(Model model) {
        model.addAttribute("juegos", juegosDb);
        return "juegos";
    }

    @GetMapping("/juegos/nuevo")
    public String mostrarFormulario() {
        return "formulario";
    }

    @PostMapping("/juegos")
    public String guardarJuego(@RequestParam("titulo") String titulo,
                               @RequestParam("descripcion") String descripcion,
                               @RequestParam("portada") MultipartFile portada) {
        String nombreArchivo = "default.png";

        //Verificamos si el archivo no está vacío
        if (!portada.isEmpty()) {
            try {
                //Tip para clase: Crear la carpeta si no existe
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                //Generamos un nombre único para evitar sobreescribir archivos con el mismo nombre
                nombreArchivo = UUID.randomUUID().toString() + "-" + portada.getOriginalFilename();
                Path filePath = uploadPath.resolve(nombreArchivo);

                // Guardamos el archivo fisicamente en el disco
                Files.copy(portada.getInputStream(), filePath);
                System.out.println("Archivo guardado en: " + filePath.toAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
                // En un proyecto real manejaramos esta excepcíon apropiadamente
            }
        }

        Juego nuevoJuego = new Juego(idCounter++, titulo, descripcion, nombreArchivo);
        juegosDb.add(nuevoJuego);
        return "redirect:/juegos";
    }

}
