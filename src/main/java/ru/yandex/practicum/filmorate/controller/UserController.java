package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dao.UserRepository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.ValidationService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final ValidationService validationService;
    private final UserRepository repository;

    public UserController(ValidationService validationService, UserRepository repository) {
        this.validationService = validationService;
        this.repository = repository;
    }


    @GetMapping
    public Collection<User> getAllUsers() {
        return repository.getUsers().values();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user, BindingResult result) {
        //Валидация
        validationService.validateUser(user, result);
        //Добавление пользователя
        repository.save(user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user, BindingResult result) {
        //Валидация
        validationService.validateUser(user, result);
        //Обновление пользователя
        repository.update(user);
        return user;
    }
}
