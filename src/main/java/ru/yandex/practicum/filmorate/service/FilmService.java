package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.film.FilmDao;
import ru.yandex.practicum.filmorate.storage.user.UserDao;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {

    private final FilmDao filmStorage;

    private final UserDao userStorage;

    public Collection<Film> getAllFilms() {
        log.info("Получаем список всех фильмов.");
        return filmStorage.findAll();
    }

    public Film save(Film film) {
        log.info("Добавляем следующий фильм: {}", film);
        return filmStorage.save(film);
    }

    public Film update(Film film) {
        log.info("Обновляем следующий фильм: {}", film);
        return filmStorage.update(film);
    }

    public Film findFilmById(Long filmId) {
        log.info("Получаем фильм с ID: {}", filmId);
        return filmStorage.findFilmById(filmId);
    }

    public void addLike(Long filmId, Long userId) {
        userStorage.findUserById(userId); // проверить наличие юзера
        log.info("Пользователь с ID: {} ставит лайк фильму с ID: {}", userId, filmId);
        filmStorage.addLike(filmId, userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        userStorage.findUserById(userId); // проверить наличие юзера
        log.info("Пользователь с ID: {} удаляет лайк к фильму с ID: {}", userId, filmId);
        filmStorage.deleteLike(filmId, userId);
    }

    public List<Film> getMostPopularFilms(Long count) {
        log.info("Получаем список самых полулярных фильмов.");
        return filmStorage.findMostPopularFilms(count);
    }

    public Collection<Genre> findAllGenres() {
        log.info("Получаем список всех жанров.");
        return filmStorage.findAllGenres();
    }

    public Genre findGenreById(Long genreId) {
        log.info("Получаем жанр по ID = {}", genreId);
        return filmStorage.findGenreById(genreId);
    }

    public Collection<MPA> findAllMPA() {
        log.info("Получаем список всех рейтингов.");
        return filmStorage.findAllMPA();
    }

    public MPA findMPAById(Long mpaId) {
        log.info("Получаем рейтинг по ID = {}", mpaId);
        return filmStorage.findMPAById(mpaId);
    }

}
