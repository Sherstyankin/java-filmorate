package ru.yandex.practicum.filmorate.exeption.film;

public class InvalidReleaseDayException extends RuntimeException {
    public InvalidReleaseDayException(String s) {
        super(s);
    }
}
