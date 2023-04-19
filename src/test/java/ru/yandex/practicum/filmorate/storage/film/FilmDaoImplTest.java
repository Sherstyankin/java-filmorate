package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Sql(scripts = "/test_data.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDaoImplTest {

    private final FilmDao filmDao;
    private Film testFilm;

    @BeforeEach
    void init() {
        testFilm = Film.builder()
                .name("Форест Гамп")
                .description("Про успех")
                .releaseDate(LocalDate.of(1994, 1, 1))
                .duration(131L)
                .mpa(new MPA(3L, "PG-13"))
                .genres(Set.of(new Genre(1L, "Комедия"), new Genre(2L, "Драма")))
                .build();
        filmDao.addLike(1L, 1L);
    }

    @Test
    void whenSaveFilmThenReturnSavedFilm() {
        Optional<Film> filmOptional = Optional.ofNullable(filmDao.save(testFilm));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 4L)
                );
    }

    @Test
    void whenUpdateFilmThenReturnUpdatedFilm() {
        testFilm.setName("Титаник");
        testFilm.setId(2L);
        Optional<Film> filmOptional = Optional.ofNullable(filmDao.update(testFilm));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 2L))
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "Титаник"));
    }

    @Test
    void whenFindAllThenReturn3Films() {
        Collection<Film> films = filmDao.findAll();
        assertThat(films.size()).isEqualTo(3);
    }

    @Test
    void whenFindFilmBy1IdThenReturnFilmWithIdFieldEquals1() {
        Optional<Film> filmOptional = Optional.ofNullable(filmDao.findFilmById(1L));
        assertThat(filmOptional).isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    void whenFindAllMPAThenReturn5Mpa() {
        Collection<MPA> mpas = filmDao.findAllMPA();
        assertThat(mpas.size()).isEqualTo(5);
    }

    @Test
    void whenFindMPAById1ThenReturnMpaWithNameG() {
        Optional<MPA> mpaOptional = Optional.ofNullable(filmDao.findMPAById(1L));
        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa).hasFieldOrPropertyWithValue("name", "G")
                );
    }

    @Test
    void whenFindAllGenresThenReturn6Genres() {
        Collection<Genre> genres = filmDao.findAllGenres();
        assertThat(genres.size()).isEqualTo(6);
    }

    @Test
    void whenFindGenreByIdThenReturnGenreWithNameComedy() {
        Optional<Genre> genreOptional = Optional.ofNullable(filmDao.findGenreById(1L));
        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("name", "Комедия")
                );
    }

    @Test
    void whenAddLikeThenReturnTrue() {
        boolean expected = filmDao.addLike(2L, 2L);
        assertTrue(expected);
    }

    @Test
    void whenDeleteLikeThenReturnTrue() {
        boolean expected = filmDao.deleteLike(1L, 1L);
        assertTrue(expected);
    }

    @Test
    void whenFindMostPopularFilmsThenReturnTerminator() {
        List<Film> filmsOptional = filmDao.findMostPopularFilms(1L);
        assertThat(filmsOptional.iterator().next().getName()).isEqualTo("Терминатор");
    }
}