package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dal.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashSet;

@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:schema.sql")
@SpringBootTest
public class FriendsDbStorageTest {
    private final UserDbStorage userDBStorage;

    @Test
    public void getAllFriendByUserIdTest() {
        User user = User.builder()
                .id(1L)
                .name("TestName")
                .email("testMail@mail.ru")
                .login("TestLogin")
                .birthday(LocalDate.of(2001, 7, 5))
                .friends(new HashSet<>())
                .build();
        User friend = User.builder()
                .id(2L)
                .name("TestNameFriend")
                .email("testMailFriend@mail.ru")
                .login("TestLoginFriend")
                .birthday(LocalDate.of(1995, 8, 1))
                .friends(new HashSet<>())
                .build();

        userDBStorage.createUser(user);
        userDBStorage.createUser(friend);
        userDBStorage.userAddFriend(1L, 2L);
        User[] expected = {friend};
        Assertions.assertArrayEquals(expected, userDBStorage.getAllFriendByUserId(1L).toArray(), "Не получили" +
                " всех друзей пользователя.");
    }

    @Test
    public void userAddFriendTest() {
        User user = User.builder()
                .id(1L)
                .name("TestName")
                .email("testMail@mail.ru")
                .login("TestLogin")
                .birthday(LocalDate.of(2001, 7, 5))
                .friends(new HashSet<>())
                .build();
        User friend = User.builder()
                .id(1L)
                .name("TestNameFriend")
                .email("testMailFriend@mail.ru")
                .login("TestLoginFriend")
                .birthday(LocalDate.of(1995, 8, 1))
                .friends(new HashSet<>())
                .build();
        userDBStorage.createUser(user);
        userDBStorage.createUser(friend);
        userDBStorage.userAddFriend(1L, 2L);
        User[] arrayFriends = new User[]{friend};
        Assertions.assertArrayEquals(arrayFriends, userDBStorage.getAllFriendByUserId(1L).toArray(), "К" +
                " пользователю друг не добавился");
    }

    @Test
    public void userDeleteFriendTest() {
        User user = User.builder()
                .id(1L)
                .name("TestName")
                .email("testMail@mail.ru")
                .login("TestLogin")
                .birthday(LocalDate.of(2001, 7, 5))
                .friends(new HashSet<>())
                .build();
        User friend = User.builder()
                .id(2L)
                .name("TestNameFriend")
                .email("testMailFriend@mail.ru")
                .login("TestLoginFriend")
                .birthday(LocalDate.of(1995, 8, 1))
                .friends(new HashSet<>())
                .build();
        userDBStorage.createUser(user);
        userDBStorage.createUser(friend);
        userDBStorage.userAddFriend(1L, 2L);
        User[] arrayFriends = {friend};
        Assertions.assertArrayEquals(arrayFriends, userDBStorage.getAllFriendByUserId(1L).toArray());
        userDBStorage.userDeleteFriend(1L, 2L);
        User[] expectedAfterDelete = {};
        Assertions.assertArrayEquals(expectedAfterDelete, userDBStorage.getAllFriendByUserId(1L).toArray(),
                "Не удалился друг у пользователя");
    }
}