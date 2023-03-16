package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.exeption.film.InvalidReleaseDayException;
import ru.yandex.practicum.filmorate.exeption.user.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class ValidationService {
    private final UserStorage userStorage;

    public void validateUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.info("Имя пользователя отсутствует, присваиваем логин: {}", user.getLogin());
            user.setName(user.getLogin());
        }
    }

    public void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new InvalidReleaseDayException("Дата релиза фильма " + film.getReleaseDate() +
                    " указана некорректно.");
        }
    }

    public void validateId(Long id) {
        if (id == null) {
            throw new ValidationException("id не может быть null.");
        }
        if (!userStorage.isUserExist(id)) {
            throw new UserNotFoundException("Пользователь c ID: " + id + " не существует!");
        }
    }

    public void validateCount(Long count) {
        if (count <= 0) {
            throw new ValidationException("Количество не может быть отрицательным.");
        }
    }
}
