package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dao.FilmRepository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.ValidationService;

import javax.validation.Valid;
import java.util.Collection;


@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final ValidationService validationService;
    private final FilmRepository repository;

    public FilmController(ValidationService validationService, FilmRepository repository) {
        this.validationService = validationService;
        this.repository = repository;
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        return repository.getFilms().values();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film, BindingResult result) {
        //Валидация
        validationService.validateFilm(film, result);
        //Добавление фильма
        repository.save(film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film, BindingResult result) {
        //Валидация
        validationService.validateFilm(film, result);
        //Обновляем фильм в базе
        repository.update(film);
        return film;
    }
}
