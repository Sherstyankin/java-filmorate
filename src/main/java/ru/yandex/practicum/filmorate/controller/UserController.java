package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exeption.UserDoesNotExistException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int generatedId = 0;
    private int getGeneratedId() {
        return ++generatedId;
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user, BindingResult result) {
        //Валидация
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (result.hasErrors()) {
            log.warn("Ошибка при создании пользователя: {}", result.getFieldErrors());
            throw new ValidationException("Ошибка при создании пользователя: " + result.getFieldErrors());
        }
        user.setId(getGeneratedId());
        if (users.containsKey(user.getId())) {
            throw new UserAlreadyExistException("Пользователь c " + user.getId() + " уже существует!");
        }
        //Добавление пользователя
        log.debug("Добавляем следующего пользователя: {}", user);
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user, BindingResult result) {
        //Валидация
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (!users.containsKey(user.getId())) {
            log.warn("Пользователь c {} не существует!", user.getId());
            throw new UserDoesNotExistException("Пользователь c " + user.getId() + " не существует!");
        }
        if (result.hasErrors()) {
            log.warn("Ошибка при обновлении пользователя: {}", result.getFieldErrors());
            throw new ValidationException("Ошибка при обновлении пользователя: " + result.getFieldErrors());
        }
        //Обновление пользователя
        log.debug("Обновляем следующего пользователя: {}", user);
        users.put(user.getId(), user);
        return user;
    }
}
