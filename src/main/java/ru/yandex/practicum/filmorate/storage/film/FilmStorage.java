package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Collection<Film> getFilms();

    boolean isFilmExist(Long filmId);

    void put(Film film);

    Film get(Long filmId);
}
