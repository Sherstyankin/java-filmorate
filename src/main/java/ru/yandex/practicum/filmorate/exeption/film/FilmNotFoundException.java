package ru.yandex.practicum.filmorate.exeption.film;

public class FilmNotFoundException extends RuntimeException {
    public FilmNotFoundException(String s) {
        super(s);
    }
}
