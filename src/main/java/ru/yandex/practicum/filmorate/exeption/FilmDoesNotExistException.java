package ru.yandex.practicum.filmorate.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FilmDoesNotExistException extends RuntimeException {
    public FilmDoesNotExistException(String s) {
        super(s);
    }
}
