package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import ru.yandex.practicum.filmorate.exeption.InvalidReleaseDayException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Component
@Slf4j
public class ValidationService {
    public void validateUser(User user, BindingResult result) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (result.hasErrors()) {
            log.warn("Ошибка при валидации пользователя: {}", result.getFieldErrors());
            throw new ValidationException("Ошибка при валидации пользователя: " + result.getFieldErrors());
        }
    }

    public void validateFilm(Film film, BindingResult result) {
        if (result.hasErrors()) {
            log.warn("Ошибка при создании фильма: {}", result.getFieldErrors());
            throw new ValidationException("Ошибка при создании фильма: " + result.getFieldErrors());
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Дата релиза фильма {} указана некорректно.", film.getReleaseDate());
            throw new InvalidReleaseDayException("Дата релиза фильма " + film.getReleaseDate() +
                    " указана некорректно.");
        }
    }
}
