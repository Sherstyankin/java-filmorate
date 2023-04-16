package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeption.user.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Repository("UserDaoImpl")
@Slf4j
public class UserDaoImpl implements UserDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<User> findAllUsers() {
        String sqlQuery = "select id, email, login, name, birthday " +
                "from users";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public User findUserById(Long userId) {
        String sqlQuery = "select id, email, login, name, birthday " +
                "from users " +
                "where id = ?";
        List<User> users = jdbcTemplate.query(sqlQuery, this::mapRowToUser, userId);
        if (users.size() != 1) {
            log.error("Пользователь с таким ID = {} не существует.", userId);
            throw new UserNotFoundException("Фильм c " + userId + " не существует!");
        }
        return users.get(0);
    }

    @Override
    public User save(User user) {
        String sqlQuery = "insert into users (email, login, name, birthday) " +
                "values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        Long userId = keyHolder.getKey().longValue();
        return findUserById(userId);
    }

    @Override
    public User update(User user) {
        findUserById(user.getId()); // проверить существует ли юзер
        String sqlQuery = "update users set " +
                "email = ?, login = ?, name = ?, birthday = ? " +
                "where id = ?";
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday()),
                user.getId());
        return findUserById(user.getId());
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        String sqlQuery = "insert into friendship (friendship_from, friendship_to) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery, friendId, userId); // у юзера(userId) будет друг(friendId)
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        String sqlQuery = "delete from friendship " +
                "where friendship_from = ? and friendship_to = ?";
        jdbcTemplate.update(sqlQuery, friendId, userId);
    }

    @Override
    public List<User> findAllUserFriends(Long userId) {
        String sqlQuery = "SELECT friendship_from " +
                "FROM FRIENDSHIP " +
                "WHERE friendship_to = ?";
        return jdbcTemplate.query(sqlQuery,
                        (rs, rowNum) -> rs.getLong("friendship_from"), userId)
                .stream()
                .map(this::findUserById)
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> findCommonFriendsId(Long userId, Long otherId) {
        String sqlQuery = "SELECT friendship_from " +
                "FROM FRIENDSHIP " +
                "WHERE friendship_to = ?";
        List<Long> friendsIdOfUser1 = jdbcTemplate.query(sqlQuery,
                (rs, rowNum) -> rs.getLong("friendship_from"), userId);
        List<Long> friendsIdOfUser2 = jdbcTemplate.query(sqlQuery,
                (rs, rowNum) -> rs.getLong("friendship_from"), otherId);
        List<Long> commonFriendsId = new ArrayList<>(friendsIdOfUser1);
        commonFriendsId.retainAll(friendsIdOfUser2);//в commonFriendsId остаются только общие id друзей
        return commonFriendsId;
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
    }
}
