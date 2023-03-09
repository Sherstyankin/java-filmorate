package ru.yandex.practicum.filmorate.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FOUND)
public class FilmAlreadyExistException extends RuntimeException {
    public FilmAlreadyExistException(String s) {
        super(s);
    }
}
