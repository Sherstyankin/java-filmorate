package ru.yandex.practicum.filmorate.exeption.user;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String s) {
        super(s);
    }
}

