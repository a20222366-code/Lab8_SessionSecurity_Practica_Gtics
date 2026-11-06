package org.example.lab8_gtics_practica_session_security.controller;

import jakarta.servlet.http.HttpSession;
import org.example.lab8_gtics_practica_session_security.model.PlaylistEntry;
import org.example.lab8_gtics_practica_session_security.service.PlaylistService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/user/history")
public class UserHistoryController {

    private final PlaylistService playlistService;

    public UserHistoryController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    // Muestra todos los registros (Agregados y Eliminados) guardados en la sesión
    @GetMapping
    public String history(Model model, HttpSession session) {
        model.addAttribute("history", playlistService.getAll(session));
        return "user/history";
    }

    // Muestra el detalle de un registro. "index" es su posición en la lista (0, 1, 2...)
    @GetMapping("/{index}")
    public String detail(@PathVariable int index, Model model,
                         HttpSession session, RedirectAttributes ra) {

        List<PlaylistEntry> history = playlistService.getAll(session);

        // Si la posición no existe, volvemos al historial con un mensaje
        if (index < 0 || index >= history.size()) {
            ra.addFlashAttribute("error", "No se encontró el registro solicitado.");
            return "redirect:/user/history";
        }

        model.addAttribute("entry", history.get(index));
        return "user/history-detail";
    }
}