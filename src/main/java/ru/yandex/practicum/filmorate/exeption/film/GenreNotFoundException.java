package ru.yandex.practicum.filmorate.exeption.film;

public class GenreNotFoundException extends RuntimeException {
    public GenreNotFoundException(String s) {
        super(s);
    }
}
