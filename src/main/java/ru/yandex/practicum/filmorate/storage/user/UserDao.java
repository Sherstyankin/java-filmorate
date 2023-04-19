package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserDao {
    Collection<User> findAllUsers();

    User findUserById(Long userId);

    User save(User user);

    User update(User user);

    boolean addFriend(Long userId, Long friendId);

    boolean deleteFriend(Long userId, Long friendId);

    List<User> findAllUserFriends(Long userId);

    List<Long> findCommonFriendsId(Long userId, Long otherId);
}
