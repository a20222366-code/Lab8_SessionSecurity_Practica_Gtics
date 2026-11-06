package org.example.lab8_gtics_practica_session_security.controller;

import jakarta.servlet.http.HttpSession;
import org.example.lab8_gtics_practica_session_security.model.PlaylistEntry;
import org.example.lab8_gtics_practica_session_security.model.Song;
import org.example.lab8_gtics_practica_session_security.service.PlaylistService;
import org.example.lab8_gtics_practica_session_security.service.SongService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/user/playlist")
public class UserPlaylistController {

    private final SongService songService;
    private final PlaylistService playlistService;

    public UserPlaylistController(SongService songService, PlaylistService playlistService) {
        this.songService = songService;
        this.playlistService = playlistService;
    }

    // Muestra las canciones disponibles y la playlist del usuario
    @GetMapping
    public String view(Model model, HttpSession session) {
        List<PlaylistEntry> active = playlistService.getActive(session);

        // Lista con los IDs de las canciones que ya están en la playlist
        List<Integer> inPlaylistIds = new ArrayList<>();
        for (PlaylistEntry entry : active) {
            inPlaylistIds.add(entry.getSong().getId());
        }

        model.addAttribute("songs", songService.findAll());
        model.addAttribute("playlist", active);
        model.addAttribute("inPlaylistIds", inPlaylistIds);
        return "user/playlist";
    }

    // Agrega una canción a la playlist
    @PostMapping("/add/{songId}")
    public String add(@PathVariable int songId, HttpSession session, RedirectAttributes ra) {
        Song song = songService.findById(songId);

        if (song == null) {
            ra.addFlashAttribute("error", "La canción no existe.");
        } else if (!playlistService.add(session, song)) {
            ra.addFlashAttribute("error", "Esa canción ya está en tu playlist.");
        } else {
            ra.addFlashAttribute("success", "Canción agregada a tu playlist.");
        }
        return "redirect:/user/playlist";
    }

    // Quita una canción de la playlist
    @PostMapping("/remove/{songId}")
    public String remove(@PathVariable int songId, HttpSession session, RedirectAttributes ra) {
        if (playlistService.remove(session, songId)) {
            ra.addFlashAttribute("success", "Canción quitada de tu playlist.");
        } else {
            ra.addFlashAttribute("error", "Esa canción no está en tu playlist.");
        }
        return "redirect:/user/playlist";
    }
}