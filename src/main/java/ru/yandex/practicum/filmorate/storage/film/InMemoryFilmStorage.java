package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();

    public Collection<Film> getFilms() {
        return films.values();
    }

    public boolean isFilmExist(Long filmId) {
        return films.containsKey(filmId);
    }

    public void put(Film film) {
        films.put(film.getId(), film);
    }

    public Film get(Long filmId) {
        return films.get(filmId);
    }

}
