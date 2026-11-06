package org.example.lab8_gtics_practica_session_security.model;

import java.io.Serializable;

public class Song implements Serializable {

    private int id;
    private String name;
    private String artist;
    private String genre;
    private String duration; // formato mm:ss

    public Song(int id, String name, String artist, String genre, String duration) {
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.genre = genre;
        this.duration = duration;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getArtist() { return artist; }
    public String getGenre() { return genre; }
    public String getDuration() { return duration; }
}
