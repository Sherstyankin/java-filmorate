package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.ValidationService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final ValidationService validationService;
    private final UserService userService;

    @GetMapping
    public Collection<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        //Валидация
        validationService.validateUser(user);
        //Добавление пользователя
        return userService.save(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        //Валидация
        validationService.validateUser(user);
        //Обновление пользователя
        return userService.update(user);
    }

    @GetMapping("/{id}") //получение юзера по id
    public User findUserById(@NotNull @PathVariable(value = "id") Long userId) {
        return userService.findUserById(userId);
    }

    @PutMapping("/{id}/friends/{friendId}") //добавление в друзья
    public void addTofriends(@NotNull @PathVariable(value = "id") Long userId,
                             @NotNull @PathVariable Long friendId) {
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}") //удаление из друзей
    public void deleteFromFriends(@NotNull @PathVariable(value = "id") Long userId,
                                  @NotNull @PathVariable Long friendId) {
        userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/{id}/friends") //получение всех друзей
    public List<User> getUserFriends(@NotNull @PathVariable(value = "id") Long userId) {
        return userService.getUserFriends(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}") //получение общих друзей
    public List<User> getCommonFriends(@NotNull @PathVariable(value = "id") Long userId,
                                       @NotNull @PathVariable Long otherId) {
        return userService.getCommonFriends(userId, otherId);
    }
}
