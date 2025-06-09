package com.pluralsight;

public class Film {

    int filmId, releaseYear, length;
    String title, description;

    public Film(int filmId, int releaseYear, int length, String title, String description) {
        this.filmId = filmId;
        this.releaseYear = releaseYear;
        this.length = length;
        this.title = title;
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format("""
                Film ID: %d
                Title: %s
                Year: %d
                Desc: %s
                Length: %d Minutes
                ------------------------------
                """, filmId, title, releaseYear, description, length);
    }
}
