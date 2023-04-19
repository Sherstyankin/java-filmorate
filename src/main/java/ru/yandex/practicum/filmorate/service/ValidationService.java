package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.film.InvalidReleaseDayException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class ValidationService {

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
}
