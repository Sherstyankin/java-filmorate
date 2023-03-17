package ru.yandex.practicum.filmorate.exeption.film;

public class FilmAlreadyExistException extends RuntimeException {
    public FilmAlreadyExistException(String s) {
        super(s);
    }
}
