package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.ValidationService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@Validated
@RequestMapping
@RequiredArgsConstructor
public class FilmController {
    private final ValidationService validationService;
    private final FilmService filmService;
    private final ModelMapper modelMapper;

    @GetMapping("/films")
    public List<FilmDto> getAllFilms() {
        return filmService.getAllFilms().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/films")
    public FilmDto createFilm(@Valid @RequestBody Film film) {
        //Валидация
        validationService.validateFilm(film);
        //Добавление фильма
        return convertToDto(filmService.save(film));
    }

    @PutMapping("/films")
    public FilmDto updateFilm(@Valid @RequestBody Film film) {
        //Валидация
        validationService.validateFilm(film);
        //Обновляем фильм в базе
        return convertToDto(filmService.update(film));
    }

    @GetMapping("/films/{id}") //получение фильма по id
    public FilmDto findFilmById(@NotNull @PathVariable(value = "id") Long filmId) {
        return convertToDto(filmService.findFilmById(filmId));
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@NotNull @PathVariable(value = "id") Long filmId,
                        @NotNull @PathVariable Long userId) {
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@NotNull @PathVariable(value = "id") Long filmId,
                           @NotNull @PathVariable Long userId) {
        filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/films/popular")
    public List<FilmDto> getMostPopularFilms(@Positive @RequestParam(defaultValue = "10",
            required = false) Long count) {
        return filmService.getMostPopularFilms(count).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/genres") //получение списка всех жанром
    public Collection<Genre> findAllGenres() {
        return filmService.findAllGenres();
    }

    @GetMapping("/genres/{id}") //получение жанра по id
    public Genre findGenreById(@NotNull @PathVariable(value = "id") Long genreId) {
        return filmService.findGenreById(genreId);
    }

    @GetMapping("/mpa") //получение списка всех возрастных рейтингов
    public Collection<MPA> findAllMPA() {
        return filmService.findAllMPA();
    }


    @GetMapping("/mpa/{id}") //получение mpa по id
    public MPA findMPAById(@NotNull @PathVariable(value = "id") Long mpaId) {
        return filmService.findMPAById(mpaId);
    }

    private FilmDto convertToDto(Film film) {
        return modelMapper.map(film, FilmDto.class);
    }
}
