package ru.yandex.practicum.filmorate.exeption.user;

public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException(String s) {
        super(s);
    }
}
