package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.Collection;
import java.util.List;

public interface FilmDao {

    Film save(Film film);

    Film update(Film film);

    Collection<Film> findAll();

    Film findFilmById(Long id);

    Collection<MPA> findAllMPA();

    MPA findMPAById(Long id);

    Collection<Genre> findAllGenres();

    Genre findGenreById(Long id);

    List<Film> findMostPopularFilms(Long count);

    void addLike(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userId);
}
