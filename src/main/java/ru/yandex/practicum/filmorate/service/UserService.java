package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserDao userStorage;

    public Collection<User> getAllUsers() {
        log.info("Получаем список всех пользователей.");
        return userStorage.findAllUsers();
    }

    public User save(User user) {
        log.info("Добавляем следующего пользователя: {}", user);
        return userStorage.save(user);
    }

    public User update(User user) {
        log.info("Обновляем следующего пользователя: {}", user);
        return userStorage.update(user);
    }

    public void addFriend(Long userId, Long friendId) {
        userStorage.findUserById(userId); // проверить наличие юзера с данным id
        userStorage.findUserById(friendId); // проверить наличие юзера с данным id
        userStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        userStorage.deleteFriend(userId, friendId);
    }

    public List<User> getUserFriends(Long userId) {
        List<User> userFriends = userStorage.findAllUserFriends(userId);
        log.info("Получаем всех друзей пользователя с ID: {} => {}", userId, userFriends);
        return userFriends; //возвращаем всех друзей юзера
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        List<User> commonFriends = new ArrayList<>();
        for (Long id : userStorage.findCommonFriendsId(userId, otherId)) {
            commonFriends.add(userStorage.findUserById(id));//достаем общих друзей
        }
        log.info("Получаем общих друзей пользователя с ID: {} " +
                "с пользователем с ID: {} => {}", userId, otherId, commonFriends);
        return commonFriends; //возвращаем общих друзей
    }

    public User findUserById(Long userId) {
        log.info("Получаем пользователя с ID: {}", userId);
        return userStorage.findUserById(userId);
    }
}
