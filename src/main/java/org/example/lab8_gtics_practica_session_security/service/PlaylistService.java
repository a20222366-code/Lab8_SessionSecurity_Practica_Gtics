package org.example.lab8_gtics_practica_session_security.service;

import jakarta.servlet.http.HttpSession;
import org.example.lab8_gtics_practica_session_security.model.PlaylistEntry;
import org.example.lab8_gtics_practica_session_security.model.Song;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
// Maneja la playlist de cada usuario, guardada en su sesión
public class PlaylistService {

    // Nombre con el que se guarda la lista en la sesión
    private String key = "playlist";

    // Devuelve TODOS los registros de la sesión (Agregados y Eliminados)
    public List<PlaylistEntry> getAll(HttpSession session) {
        List<PlaylistEntry> list = (List<PlaylistEntry>) session.getAttribute(key);
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    // Devuelve solo los registros con estado "Agregado"
    public List<PlaylistEntry> getActive(HttpSession session) {
        List<PlaylistEntry> active = new ArrayList<>();
        for (PlaylistEntry entry : getAll(session)) {
            if (entry.getStatus().equals("Agregado")) {
                active.add(entry);
            }
        }
        return active;
    }

    // Revisa si la canción ya está en la playlist (con estado "Agregado")
    public boolean isInPlaylist(HttpSession session, int songId) {
        for (PlaylistEntry entry : getAll(session)) {
            if (entry.getStatus().equals("Agregado") && entry.getSong().getId() == songId) {
                return true;
            }
        }
        return false;
    }

    // Agrega la canción. Devuelve false si ya estaba en la playlist
    public boolean add(HttpSession session, Song song) {
        if (isInPlaylist(session, song.getId())) {
            return false;
        }
        List<PlaylistEntry> list = getAll(session);
        list.add(new PlaylistEntry(song));
        session.setAttribute(key, list); // se vuelve a guardar para que Spring Session lo actualice
        return true;
    }

    // Quita la canción: no se borra, solo cambia su estado a "Eliminado"
    public boolean remove(HttpSession session, int songId) {
        List<PlaylistEntry> list = getAll(session);
        for (PlaylistEntry entry : list) {
            if (entry.getStatus().equals("Agregado") && entry.getSong().getId() == songId) {
                entry.setStatus("Eliminado");
                session.setAttribute(key, list); // se vuelve a guardar
                return true;
            }
        }
        return false;
    }
}