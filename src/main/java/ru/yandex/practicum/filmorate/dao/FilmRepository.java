package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.FilmAlreadyExistException;
import ru.yandex.practicum.filmorate.exeption.FilmDoesNotExistException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class FilmRepository {
    private final Map<Integer, Film> films = new HashMap<>();
    private int generatedId = 0;

    private int getGeneratedId() {
        return ++generatedId;
    }

    public Map<Integer, Film> getFilms() {
        return films;
    }

    public void save(Film film) {
        film.setId(getGeneratedId());
        if (films.containsKey(film.getId())) {
            throw new FilmAlreadyExistException("Фильм c " + film.getId() + " уже существует!");
        }
        log.info("Добавляем следующий фильм: {}", film);
        films.put(film.getId(), film);
    }

    public void update(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new FilmDoesNotExistException("Фильм c " + film.getId() + " не существует!");
        }
        log.info("Обновляем следующий фильм: {}", film);
        films.put(film.getId(), film);
    }
}
