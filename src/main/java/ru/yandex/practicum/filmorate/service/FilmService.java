package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.film.FilmAlreadyExistException;
import ru.yandex.practicum.filmorate.exeption.film.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private Long generatedId = 0L;

    private Long getGeneratedId() {
        return ++generatedId;
    }

    public Collection<Film> getAllFilms() {
        log.info("Получаем список всех фильмов.");
        return filmStorage.getFilms();
    }

    public void save(Film film) {
        film.setId(getGeneratedId());
        if (filmStorage.isFilmExist(film.getId())) {
            throw new FilmAlreadyExistException("Фильм c " + film.getId() + " уже существует!");
        }
        log.info("Добавляем следующий фильм: {}", film);
        filmStorage.put(film);
    }

    public void update(Film film) {
        if (!filmStorage.isFilmExist(film.getId())) {
            throw new FilmNotFoundException("Фильм c " + film.getId() + " не существует!");
        }
        log.info("Обновляем следующий фильм: {}", film);
        filmStorage.put(film);
    }

    public Film findFilmById(Long filmId) {
        log.info("Получаем фильм с ID: {}", filmId);
        return filmStorage.get(filmId);
    }

    public void addLike(Long filmId, Long userId) {
        Film film = filmStorage.get(filmId);
        log.info("Пользователь с ID: {} ставит лайк фильму с ID: {}", userId, filmId);
        film.getLikes().add(userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        Film film = filmStorage.get(filmId);
        log.info("Пользователь с ID: {} удаляет лайк к фильму с ID: {}", userId, filmId);
        film.getLikes().remove(userId);
    }

    public List<Film> getMostPopularFilms(Long count) {
        log.info("Получаем список самых полулярных фильмов");
        return filmStorage.getFilms().stream()
                .sorted((f0, f1) -> -1 * Integer.compare(f0.getLikes().size(), f1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
