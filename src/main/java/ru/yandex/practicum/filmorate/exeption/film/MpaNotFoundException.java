package ru.yandex.practicum.filmorate.exeption.film;

public class MpaNotFoundException extends RuntimeException {
    public MpaNotFoundException(String s) {
        super(s);
    }
}
