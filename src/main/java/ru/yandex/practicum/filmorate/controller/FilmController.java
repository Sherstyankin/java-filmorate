package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.ValidationService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;


@RestController
@RequestMapping
@RequiredArgsConstructor
public class FilmController {
    private final ValidationService validationService;
    private final FilmService filmService;

    @GetMapping("/films")
    public Collection<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @PostMapping("/films")
    public Film createFilm(@Valid @RequestBody Film film) {
        //Валидация
        validationService.validateFilm(film);
        //Добавление фильма
        return filmService.save(film);
    }

    @PutMapping("/films")
    public Film updateFilm(@Valid @RequestBody Film film) {
        //Валидация
        validationService.validateFilm(film);
        //Обновляем фильм в базе
        return filmService.update(film);
    }

    @GetMapping("/films/{id}") //получение фильма по id
    public Film findFilmById(@PathVariable(value = "id") Long filmId) {
        validationService.validateId(filmId);
        return filmService.findFilmById(filmId);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable(value = "id") Long filmId,
                        @PathVariable Long userId) {
        validationService.validateId(filmId);
        validationService.validateId(userId);
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable(value = "id") Long filmId,
                           @PathVariable Long userId) {
        validationService.validateId(filmId);
        validationService.validateId(userId);
        filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getMostPopularFilms(@RequestParam(defaultValue = "10", required = false) Long count) {
        validationService.validateCount(count);
        return filmService.getMostPopularFilms(count);
    }

    @GetMapping("/genres") //получение списка всех жанром
    public Collection<Genre> findAllGenres() {
        return filmService.findAllGenres();
    }

    @GetMapping("/genres/{id}") //получение жанра по id
    public Genre findGenreById(@PathVariable(value = "id") Long genreId) {
        validationService.validateId(genreId);
        return filmService.findGenreById(genreId);
    }

    @GetMapping("/mpa") //получение списка всех возрастных рейтингов
    public Collection<MPA> findAllMPA() {
        return filmService.findAllMPA();
    }


    @GetMapping("/mpa/{id}") //получение mpa по id
    public MPA findMPAById(@PathVariable(value = "id") Long mpaId) {
        return filmService.findMPAById(mpaId);
    }
}
