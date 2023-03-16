package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    public Collection<User> getUsers() {
        return users.values();
    }

    public boolean isUserExist(Long userId) {
        return users.containsKey(userId);
    }

    public void put(User user) {
        users.put(user.getId(), user);
    }

    public User get(Long userId) {
        return users.get(userId);
    }
}
