package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.FilmAlreadyExistException;
import ru.yandex.practicum.filmorate.exeption.FilmDoesNotExistException;
import ru.yandex.practicum.filmorate.exeption.InvalidReleaseDayException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int generatedId = 0;
    private int getGeneratedId() {
        return ++generatedId;
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film, BindingResult result) {
        //Валидация
        if (result.hasErrors()) {
            log.warn("Ошибка при создании фильма: {}", result.getFieldErrors());
            throw new ValidationException("Ошибка при создании фильма: " + result.getFieldErrors());
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Дата релиза фильма {} указана некорректно.", film.getReleaseDate());
            throw new InvalidReleaseDayException("Дата релиза фильма " + film.getReleaseDate() +
                    " указана некорректно.");
        }
        film.setId(getGeneratedId());
        if (films.containsKey(film.getId())) {
            throw new FilmAlreadyExistException("Фильм c " + film.getId() + " уже существует!");
        }
        //Добавляем фильм в базу
        log.debug("Добавляем следующий фильм: {}", film);
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film, BindingResult result) {
        //Валидация
        if (result.hasErrors()) {
            log.warn("Ошибка при обновлении фильма: {}", result.getFieldErrors());
            throw new ValidationException("Ошибка при обновлении фильма: " + result.getFieldErrors());
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Дата релиза фильма {} указана некорректно.", film.getReleaseDate());
            throw new ValidationException("Дата релиза фильма " + film.getReleaseDate() +
                    " указана некорректно.");
        }
        if (!films.containsKey(film.getId())) {
            throw new FilmDoesNotExistException("Фильм c " + film.getId() + " не существует!");
        }
        //Обновляем фильм в базе
        log.debug("Обновляем следующий фильм: {}", film);
        films.put(film.getId(), film);
        return film;
    }
}
