package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Sql(scripts = "/test_data.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDaoImplTest {

    private final UserDao userDao;
    private User testUser;

    @BeforeEach
    void init() {
        userDao.addFriend(2L, 1L);
        userDao.addFriend(2L, 3L);
        userDao.addFriend(3L, 1L);
        testUser = User.builder()
                .email("sasha@mail.com")
                .login("son")
                .name("Sasha")
                .birthday(LocalDate.of(2002, 12, 12))
                .build();
    }

    @Test
    void whenFindAllUsersThenReturn3Users() {
        Collection<User> users = userDao.findAllUsers();
        assertThat(users.size()).isEqualTo(3);
    }

    @Test
    void whenFindUserById1ThenReturnLoginGrey() {
        Optional<User> userOptional = Optional.ofNullable(userDao.findUserById(1L));
        assertThat(userOptional).isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("login", "grey")
                );
    }

    @Test
    void save() {
        Optional<User> userOptional = Optional.ofNullable(userDao.save(testUser));
        assertThat(userOptional).isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 4L)
                );
    }

    @Test
    void update() {
        testUser.setId(2L);
        testUser.setName("Alexander");
        Optional<User> userOptional = Optional.ofNullable(userDao.update(testUser));
        assertThat(userOptional).isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 2L))
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "Alexander"));
    }

    @Test
    void whenAddFriendThenReturnTrue() {
        boolean expected = userDao.addFriend(1L, 2L);
        assertTrue(expected);
    }

    @Test
    void whenDeleteFriendThenReturnTrue() {
        boolean expected = userDao.deleteFriend(2L, 1L);
        assertTrue(expected);
    }

    @Test
    void whenFindAllUser2FriendsThenReturn2Friend() {
        Collection<User> userFriends = userDao.findAllUserFriends(2L);
        assertThat(userFriends.size()).isEqualTo(2);
    }

    @Test
    void whenFindCommonFriendsId() {
        List<Long> commonFriendsId = userDao.findCommonFriendsId(2L, 3L);
        assertThat(commonFriendsId.iterator().next()).isEqualTo(1L);
    }
}