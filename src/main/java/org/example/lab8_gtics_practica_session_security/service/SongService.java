package org.example.lab8_gtics_practica_session_security.service;

import org.example.lab8_gtics_practica_session_security.model.Song;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
// Guarda las canciones en memoria (sin base de datos)
public class SongService {

    // Lista donde se guardan todas las canciones registradas por el ADMIN
    private List<Song> songs = new ArrayList<>();

    // Número que se usa como ID de la siguiente canción (1, 2, 3...)
    private int nextId = 1;

    // Devuelve todas las canciones
    public List<Song> findAll() {
        return songs;
    }

    // Busca una canción por su ID. Si no la encuentra, devuelve null
    public Song findById(int id) {
        for (Song song : songs) {
            if (song.getId() == id) {
                return song;
            }
        }
        return null;
    }

    // Crea una canción nueva y la agrega a la lista
    public Song save(String name, String artist, String genre, String duration) {
        Song song = new Song(nextId, name.trim(), artist.trim(), genre.trim(), duration.trim());
        nextId++;
        songs.add(song);
        return song;
    }
}