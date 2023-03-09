package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exeption.UserDoesNotExistException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class UserRepository {
    private final Map<Integer, User> users = new HashMap<>();
    private int generatedId = 0;

    private int getGeneratedId() {
        return ++generatedId;
    }

    public Map<Integer, User> getUsers() {
        return users;
    }

    public void save(User user) {
        user.setId(getGeneratedId());
        if (users.containsKey(user.getId())) {
            throw new UserAlreadyExistException("Пользователь c " + user.getId() + " уже существует!");
        }
        log.info("Добавляем следующего пользователя: {}", user);
        users.put(user.getId(), user);
    }

    public void update(User user) {
        if (!users.containsKey(user.getId())) {
            log.warn("Пользователь c {} не существует!", user.getId());
            throw new UserDoesNotExistException("Пользователь c " + user.getId() + " не существует!");
        }
        log.info("Обновляем следующего пользователя: {}", user);
        users.put(user.getId(), user);
    }
}
