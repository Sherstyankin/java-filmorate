package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private User userToCreate;

    @BeforeEach
    protected void init() {
        userToCreate = User.builder()
                .email("mail@mail.ru")
                .login("dolore")
                .name("Nick Name")
                .birthday(LocalDate.of(1988, 7, 10))
                .build();
    }

    //Test POST validation
    @SneakyThrows
    @Test
    void whenValidInputThenReturnsOkInPost() {
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userToCreate))
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void whenNameIsNullThenReturnsOkAndNameEqualsLoginInPost() {
        userToCreate.setName(null);
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userToCreate))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("dolore"));
    }

    @SneakyThrows
    @Test
    void whenEmailIsNullValueThenReturnsBadRequestInPost() {
        userToCreate.setEmail(null);
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userToCreate))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void whenEmailHasInvalidValueThenReturnsBadRequestInPost() {
        userToCreate.setEmail("mail.ru");
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userToCreate))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void whenLoginIsNullThenReturnsBadRequestInPost() {
        userToCreate.setLogin(null);
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userToCreate))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void whenBirthdayIsInFutureThenReturnsBadRequestAndNameEqualsLoginInPost() {
        userToCreate.setBirthday(LocalDate.of(2899, 7, 10));
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userToCreate))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }



    //Test PUT validation
    @SneakyThrows
    @Test
    void whenValidInputThenReturnsOkInPut() {
        mockMvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(userToCreate))
                .contentType("application/json"));
        User userToUpdate = User.builder()
                .id(1)
                .email("rambler@mail.ru")
                .login("crim")
                .name("John Name")
                .birthday(LocalDate.of(1989, 7, 10))
                .build();
        String response = mockMvc.perform(put("/users")
                        .content(objectMapper.writeValueAsString(userToUpdate))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.writeValueAsString(userToUpdate), response);
    }

    @SneakyThrows
    @Test
    void whenEmailIsNullValueThenReturnsBadRequestInPut() {
        mockMvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(userToCreate))
                .contentType("application/json"));
        User userToUpdate = User.builder()
                .id(1)
                .email(null)
                .login("crim")
                .name("John Name")
                .birthday(LocalDate.of(1989, 7, 10))
                .build();
        mockMvc.perform(put("/users")
                        .content(objectMapper.writeValueAsString(userToUpdate))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void whenEmailHasInvalidValueThenReturnsBadRequestInPut() {
        mockMvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(userToCreate))
                .contentType("application/json"));
        User userToUpdate = User.builder()
                .id(1)
                .email("mail.ru")
                .login("crim")
                .name("John Name")
                .birthday(LocalDate.of(1989, 7, 10))
                .build();
        mockMvc.perform(put("/users")
                        .content(objectMapper.writeValueAsString(userToUpdate))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void whenPassInvalidIdThenReturnsNotFoundInPut() {
        mockMvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(userToCreate))
                .contentType("application/json"));
        User userToUpdate = User.builder()
                .id(124)
                .email("rambler@mail.ru")
                .login("crim")
                .name("John Name")
                .birthday(LocalDate.of(1989, 7, 10))
                .build();
        mockMvc.perform(put("/users")
                        .content(objectMapper.writeValueAsString(userToUpdate))
                        .contentType("application/json"))
                .andExpect(status().isNotFound());
    }
}