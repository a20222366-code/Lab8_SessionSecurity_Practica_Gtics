package org.example.lab8_gtics_practica_session_security.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class PlaylistEntry implements Serializable {

    private Song song;
    private LocalDateTime addedAt;
    private String status; // "Agregado" o "Eliminado"

    public PlaylistEntry(Song song) {
        this.song = song;
        this.addedAt = LocalDateTime.now();
        this.status = "Agregado";
    }

    public Song getSong() { return song; }
    public LocalDateTime getAddedAt() { return addedAt; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}