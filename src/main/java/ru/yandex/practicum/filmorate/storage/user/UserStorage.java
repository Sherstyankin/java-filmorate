package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    Collection<User> getUsers();

    boolean isUserExist(Long userId);

    void put(User user);

    User get(Long userId);
}
