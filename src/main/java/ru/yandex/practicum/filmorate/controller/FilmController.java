package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.ValidationService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;


@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final ValidationService validationService;
    private final FilmService filmService;

    @GetMapping
    public Collection<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        //Валидация
        validationService.validateFilm(film);
        //Добавление фильма
        filmService.save(film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        //Валидация
        validationService.validateFilm(film);
        //Обновляем фильм в базе
        filmService.update(film);
        return film;
    }

    @GetMapping("/{id}") //получение фильма по id
    public Film findFilmById(@PathVariable(value = "id") Long filmId) {
        validationService.validateId(filmId);
        return filmService.findFilmById(filmId);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable(value = "id") Long filmId,
                        @PathVariable Long userId) {
        validationService.validateId(filmId);
        validationService.validateId(userId);
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable(value = "id") Long filmId,
                           @PathVariable Long userId) {
        validationService.validateId(filmId);
        validationService.validateId(userId);
        filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getMostPopularFilms(@RequestParam(defaultValue = "10", required = false) Long count) {
        validationService.validateCount(count);
        return filmService.getMostPopularFilms(count);
    }
}
