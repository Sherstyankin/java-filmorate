package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeption.film.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exeption.film.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exeption.film.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository("FilmDaoImpl")
@Slf4j
public class FilmDaoImpl implements FilmDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film save(Film film) {
        String sqlQuery = "insert into films (name, description, release_date, duration, mpa_id) " +
                "values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setLong(4, film.getDuration());
            stmt.setLong(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        Long filmId = keyHolder.getKey().longValue();
        saveFilmGenres(film, filmId); // сохранить пару значений (genre_id и film_id) в таблицу film_genres
        return findFilmById(filmId);
    }

    @Override
    public Film update(Film film) {
        findFilmById(film.getId()); // проверить существует ли фильм
        String sqlQuery = "update films set " +
                "name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ?" +
                "where id = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        deleteFilmGenres(film); // удаляем прежние жанры фильма
        saveFilmGenres(film, film.getId()); // сохраняем новые жанры
        return findFilmById(film.getId());
    }

    @Override
    public Collection<Film> findAll() {
        String sqlQuery = "select id, name, description, release_date, duration, mpa_id " +
                "from films";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public Film findFilmById(Long id) {
        String sqlQuery = "SELECT id, name, description, release_date, duration, mpa_id " +
                "FROM films " +
                "WHERE id = ?";
        List<Film> films = jdbcTemplate.query(sqlQuery, this::mapRowToFilm, id);
        if (films.size() != 1) {
            log.error("Фильм с таким ID = {} не существует.", id);
            throw new FilmNotFoundException("Фильм c " + id + " не существует!");
        }
        return films.get(0);
    }

    @Override
    public Collection<MPA> findAllMPA() {
        String sqlQuery = "select id, name " +
                "from mpa";
        return jdbcTemplate.query(sqlQuery, this::mapRowToMPA);
    }

    @Override
    public MPA findMPAById(Long id) {
        String sqlQuery = "select id, name " +
                "from mpa where id = ?";
        List<MPA> mpas = jdbcTemplate.query(sqlQuery, this::mapRowToMPA, id);
        if (mpas.size() != 1) {
            log.error("Жанра с таким ID = {} не существует.", id);
            throw new MpaNotFoundException("Жанр c ID = " + id + " не существует!");
        }
        return mpas.get(0);
    }

    @Override
    public Collection<Genre> findAllGenres() {
        String sqlQuery = "select id, name " +
                "from genres";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
    }

    @Override
    public Genre findGenreById(Long id) {
        String sqlQuery = "select id, name " +
                "from genres " +
                "where id = ?";
        List<Genre> genres = jdbcTemplate.query(sqlQuery, this::mapRowToGenre, id);
        if (genres.size() != 1) {
            log.error("Жанра с таким ID = {} не существует.", id);
            throw new GenreNotFoundException("Жанр c ID = " + id + " не существует!");
        }
        return genres.get(0);
    }

    @Override
    public boolean addLike(Long filmId, Long userId) {
        String sqlQuery = "insert into likes (film_id, user_id) " +
                "values (?, ?)";
        return jdbcTemplate.update(sqlQuery, filmId, userId) > 0; //возврат boolean для тестов
    }

    @Override
    public boolean deleteLike(Long filmId, Long userId) {
        String sqlQuery = "delete from likes " +
                "where film_id = ? and user_id = ?";
        return jdbcTemplate.update(sqlQuery, filmId, userId) > 0; //возврат boolean для тестов
    }

    @Override
    public List<Film> findMostPopularFilms(Long count) {
        String sqlQuery = "SELECT film_id, COUNT(film_id) " +
                "FROM likes " +
                "GROUP BY film_id " +
                "ORDER BY COUNT(film_id) DESC " +
                "LIMIT ?";
        List<Long> filmIds = jdbcTemplate.query(sqlQuery, this::mapRowToFilmId, count);
        if (filmIds.isEmpty()) {
            return new ArrayList<>(findAll()); // если лайков нет, то возвращаем все фильмы
        }
        return filmIds.stream()
                .map(this::findFilmById)
                .collect(Collectors.toList());
    }

    private Long mapRowToFilmId(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getLong("film_id");
    }

    private void saveFilmGenres(Film film, Long filmId) {
        if (film.getGenres() == null || film.getGenres().isEmpty()) {
            return;
        }
        String sqlQuery = "insert into film_genres (genre_id, film_id) " +
                "values (?, ?)";
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(sqlQuery,
                    genre.getId(),
                    filmId);
        }
    }

    private void deleteFilmGenres(Film film) {
        String sqlQuery = "delete from film_genres " +
                "where film_id = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
    }

    private Set<Genre> findFilmGenres(Long filmId) {
        String sqlQuery = "select genre_id " +
                "from film_genres " +
                "where film_id = ?";
        return jdbcTemplate.query(sqlQuery, this::mapToGenreId, filmId).stream()
                .map(this::findGenreById)
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparingLong(Genre::getId))));
    }

    private Long mapToGenreId(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getLong("genre_id");
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .build();
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getLong("duration"))
                .genres(findFilmGenres(resultSet.getLong("id")))
                .mpa(findMPAById(resultSet.getLong("mpa_id")))
                .build();
    }

    private MPA mapRowToMPA(ResultSet resultSet, int rowNum) throws SQLException {
        return MPA.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
