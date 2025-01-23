package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dal.UserDbStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashSet;

@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:schema.sql")
@SpringBootTest

public class UserDbStorageTest {
    private final UserDbStorage userDBStorage;

    @Test
    public void getAllUsersTest() {
        User user = User.builder()
                .id(1L)
                .name("TestName")
                .email("testMail@mail.ru")
                .login("TestLogin")
                .birthday(LocalDate.of(2001, 7, 5))
                .friends(new HashSet<>())
                .build();
        User userTwo = User.builder()
                .id(2L)
                .name("TestNameTwo")
                .email("testMailTwo@mail.ru")
                .login("TestLoginTwo")
                .birthday(LocalDate.of(2002, 8, 7))
                .friends(new HashSet<>())
                .build();
        userDBStorage.createUser(user);
        userDBStorage.createUser(userTwo);
        Assertions.assertEquals(2, userDBStorage.findAllUsers().size(), "Неверное количество добавленных пользователей.");
    }

    @Test
    public void getUserByIdTest() {
        User user = User.builder()
                .id(1L)
                .name("TestName")
                .email("testMail@mail.ru")
                .login("TestLogin")
                .birthday(LocalDate.of(2001, 7, 5))
                .friends(new HashSet<>())
                .build();
        userDBStorage.createUser(user);
        Assertions.assertEquals(user, userDBStorage.getUserById(1L), "Не получили пользователя по id");
    }

    @Test
    public void saveUserTest() {
        User user = User.builder()
                .id(1L)
                .name("TestName")
                .email("testMail@mail.ru")
                .login("TestLogin")
                .birthday(LocalDate.of(2001, 7, 5))
                .friends(new HashSet<>())
                .build();
        userDBStorage.createUser(user);
        Assertions.assertEquals(user, userDBStorage.getUserById(1L), "Пользователи не совпадают");
    }

    @Test
    public void updateUserTest() {
        User user = User.builder()
                .id(1L)
                .name("TestName")
                .email("testMail@mail.ru")
                .login("TestLogin")
                .birthday(LocalDate.of(2001, 7, 5))
                .friends(new HashSet<>())
                .build();
        userDBStorage.createUser(user);
        user.setEmail("newMail@mail.ru");
        userDBStorage.updateUser(user);
        Assertions.assertEquals(user, userDBStorage.getUserById(1L), "Не обновился пользователь");
    }
}
