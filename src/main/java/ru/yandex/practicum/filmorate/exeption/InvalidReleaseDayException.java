package ru.yandex.practicum.filmorate.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidReleaseDayException extends RuntimeException {
    public InvalidReleaseDayException(String s) {
        super(s);
    }
}
