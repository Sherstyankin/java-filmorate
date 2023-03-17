package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FilmController.class)
@ComponentScan({"ru.yandex.practicum.filmorate"})
class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private Film filmToCreate;

    @BeforeEach
    protected void init() {
        filmToCreate = Film.builder()
                .name("Titanic")
                .description("Description")
                .duration(180L)
                .releaseDate(LocalDate.of(1997, 2, 2))
                .build();
    }

    //Test POST validation
    @SneakyThrows
    @Test
    void whenFilmValidInputThenReturnsOkInPost() {
        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(filmToCreate))
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void whenNameIsNullThenReturnsBadRequestInPost() {
        filmToCreate.setName(null);
        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(filmToCreate))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void whenDecriptionIsOver200ValueThenReturnsBadRequestInPost() {
        filmToCreate.setDescription("Descriptionnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn" +
                "nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn" +
                "nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn" +
                "nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn");
        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(filmToCreate))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void whenReleaseDateInvalidThenReturnsBadRequestInPost() {
        filmToCreate.setReleaseDate(LocalDate.of(1700, 2, 2));
        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(filmToCreate))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void whenFilmDurationNegativeThenReturnsBadRequestInPost() {
        filmToCreate.setDuration(-1L);
        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(filmToCreate))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }


    //Test PUT validation
    @SneakyThrows
    @Test
    void whenFilmValidInputThenReturnsOkInPut() {
        mockMvc.perform(post("/films")
                .content(objectMapper.writeValueAsString(filmToCreate))
                .contentType("application/json"));
        Film filmToUpdate = Film.builder()
                .id(1L)
                .name("007")
                .description("Description")
                .duration(180L)
                .releaseDate(LocalDate.of(1997, 2, 2))
                .build();
        String response = mockMvc.perform(put("/films")
                        .content(objectMapper.writeValueAsString(filmToUpdate))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.writeValueAsString(filmToUpdate), response);
    }

    @SneakyThrows
    @Test
    void whenNameIsNullValueThenReturnsBadRequestInPut() {
        mockMvc.perform(post("/films")
                .content(objectMapper.writeValueAsString(filmToCreate))
                .contentType("application/json"));
        Film filmToUpdate = Film.builder()
                .id(1L)
                .name(null)
                .description("Description")
                .duration(180L)
                .releaseDate(LocalDate.of(1997, 2, 2))
                .build();
        mockMvc.perform(put("/films")
                        .content(objectMapper.writeValueAsString(filmToUpdate))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void whenDescriptionIsOver200ValueThenReturnsBadRequestInPut() {
        mockMvc.perform(post("/films")
                .content(objectMapper.writeValueAsString(filmToCreate))
                .contentType("application/json"));
        Film filmToUpdate = Film.builder()
                .id(1L)
                .name("Titanic")
                .description("Descriptionnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn" +
                        "nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn" +
                        "nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn" +
                        "nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn")
                .duration(180L)
                .releaseDate(LocalDate.of(1997, 2, 2))
                .build();
        mockMvc.perform(put("/films")
                        .content(objectMapper.writeValueAsString(filmToUpdate))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void whenReleaseDateInvalidThenReturnsBadRequestInPut() {
        mockMvc.perform(post("/films")
                .content(objectMapper.writeValueAsString(filmToCreate))
                .contentType("application/json"));
        Film filmToUpdate = Film.builder()
                .id(1L)
                .name("Titanic")
                .description("Description")
                .duration(180L)
                .releaseDate(LocalDate.of(1700, 2, 2))
                .build();
        mockMvc.perform(put("/films")
                        .content(objectMapper.writeValueAsString(filmToUpdate))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void whenFilmDurationNegativeThenReturnsBadRequestInPut() {
        mockMvc.perform(post("/films")
                .content(objectMapper.writeValueAsString(filmToCreate))
                .contentType("application/json"));
        Film filmToUpdate = Film.builder()
                .id(1L)
                .name("Titanic")
                .description("Description")
                .duration(-1L)
                .releaseDate(LocalDate.of(1700, 2, 2))
                .build();
        mockMvc.perform(put("/films")
                        .content(objectMapper.writeValueAsString(filmToUpdate))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }
}
