package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.user.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exeption.user.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    private Long generatedId = 0L;

    private Long getGeneratedId() {
        return ++generatedId;
    }

    public Collection<User> getAllUsers() {
        log.info("Получаем список всех пользователей.");
        return userStorage.getUsers();
    }

    public void save(User user) {
        user.setId(getGeneratedId());
        if (userStorage.isUserExist(user.getId())) {
            throw new UserAlreadyExistException("Пользователь c " + user.getId() + " уже существует!");
        }
        log.info("Добавляем следующего пользователя: {}", user);
        userStorage.put(user);
    }

    public void update(User user) {
        if (!userStorage.isUserExist(user.getId())) {
            log.warn("Пользователь c {} не существует!", user.getId());
            throw new UserNotFoundException("Пользователь c " + user.getId() + " не существует!");
        }
        log.info("Обновляем следующего пользователя: {}", user);
        userStorage.put(user);
    }

    public void addFriend(Long userId, Long friendId) {
        User user1 = userStorage.get(userId); //достаем юзера из мапы по userId
        log.info("Добавляем пользователя с ID: {} в друзья пользователя с ID: {}", friendId, userId);
        user1.getFriendsId().add(friendId); //добавляем юзеру нового друга
        User user2 = userStorage.get(friendId); //достаем друга из мапы по friendId
        log.info("Добавляем пользователя с ID: {} в друзья пользователя с ID: {}", userId, friendId);
        user2.getFriendsId().add(userId); //добавляем другу юзера
    }

    public void deleteFriend(Long userId, Long friendId) {
        User user1 = userStorage.get(userId); //достаем юзера из мапы по userId
        log.info("Удаляем пользователя с ID: {} в друзья пользователя с ID: {}", friendId, userId);
        user1.getFriendsId().remove(friendId); //удаляем друга
        User user2 = userStorage.get(friendId); //достаем юзера из мапы по userId
        log.info("Удаляем пользователя с ID: {} в друзья пользователя с ID: {}", userId, friendId);
        user2.getFriendsId().remove(userId); //удаляем друга
    }

    public List<User> getUserFriends(Long userId) {
        User user = userStorage.get(userId);
        Set<Long> friendsId = user.getFriendsId();
        List<User> userFriends = new ArrayList<>();
        for (Long id : friendsId) {
            userFriends.add(userStorage.get(id));//достаем друзей
        }
        log.info("Получаем всех друзей пользователя с ID: {} => {}", userId, userFriends);
        return userFriends; //возвращаем всех друзей юзера
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        User user1 = userStorage.get(userId); //достаем user1 из мапы по userId
        User user2 = userStorage.get(otherId); //достаем user2 из мапы по otherId
        Set<Long> friendsIdOfUser1 = user1.getFriendsId();//получаем все id друзей user1
        Set<Long> friendsIdOfUser2 = user2.getFriendsId();//получаем все id друзей user2
        Set<Long> commonFriendsId = new HashSet<>(friendsIdOfUser1);
        commonFriendsId.retainAll(friendsIdOfUser2);//в commonFriendsId остаются только общие id друзей
        List<User> commonFriends = new ArrayList<>();
        for (Long id : commonFriendsId) {
            commonFriends.add(userStorage.get(id));//достаем общих друзей
        }
        log.info("Получаем общих друзей пользователя с ID: {} " +
                "с пользователем с ID: {} => {}", userId, otherId, commonFriends);
        return commonFriends; //возвращаем общих друзей
    }

    public User findUserById(Long userId) {
        log.info("Получаем пользователя с ID: {}", userId);
        return userStorage.get(userId);
    }
}
