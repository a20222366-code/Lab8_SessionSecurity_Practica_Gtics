package org.example.lab8_gtics_practica_session_security.controller;

import org.example.lab8_gtics_practica_session_security.service.SongService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Indica que esta clase es un Controlador Web (atiente las pantallas y vistas de la app)
@Controller
// Ruta raíz para esta sección. Gracias a Spring Security, solo los usuarios con rol ADMIN podrán entrar aquí
@RequestMapping("/admin/songs")
public class AdminSongController {

    // Trae el cable de conexión al Servicio de canciones para poder usar sus funciones
    private final SongService songService;

    // Constructor que conecta el servicio automáticamente al iniciar el controlador
    public AdminSongController(SongService songService) {
        this.songService = songService;
    }

    // Método que responde al entrar a la ruta principal "/admin/songs" usando GET
    @GetMapping
    public String list(Model model) {
        // Usa el objeto 'Model' (la bandeja) para llevar la lista completa de canciones hacia el HTML
        model.addAttribute("songs", songService.findAll());
        // Muestra la vista HTML que se encuentra en la carpeta 'src/main/resources/templates/admin/songs.html'
        return "admin/songs";
    }

    // Método que procesa los datos del formulario enviados por POST a la ruta "/admin/songs/new"
    @PostMapping("/new")
    public String create(@RequestParam String name,         // Captura el dato de la caja de texto 'name'
                         @RequestParam String artist,       // Captura la caja 'artist'
                         @RequestParam String genre,        // Captura la caja 'genre'
                         @RequestParam String duration,     // Captura la caja 'duration'
                         RedirectAttributes ra) {           // Herramienta especial para mandar alertas de éxito o error que sobreviven a un redireccionamiento

        // Validación 1: Revisa si alguno de los campos de texto está vacío o lleno solo de espacios
        if (name.isBlank() || artist.isBlank() || genre.isBlank() || duration.isBlank()) {
            // Guarda un mensaje flash de error de un solo uso que se pintará en la pantalla
            ra.addFlashAttribute("error", "Todos los campos son obligatorios.");
            // Recarga la página mandándolo de vuelta a la lista principal
            return "redirect:/admin/songs";
        }

        // Validación 2: Se asegura de que el texto de la duración contenga obligatoriamente los dos puntos (:)
        if (!duration.contains(":")) {
            ra.addFlashAttribute("error", "La duración debe tener el formato mm:ss (ej. 3:45).");
            return "redirect:/admin/songs";
        }

        // Si pasó todas las validaciones, le ordena al servicio que guarde la canción en memoria
        songService.save(name, artist, genre, duration);

        // Guarda un mensaje flash de éxito para avisarle al usuario que todo salió genial
        ra.addFlashAttribute("success", "Canción registrada correctamente.");

        // Redirige al usuario a la lista principal para que vea su nueva canción cargada
        return "redirect:/admin/songs";
    }
}
