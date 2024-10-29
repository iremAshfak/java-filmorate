package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
public class UserControllerTest {
    @Autowired
    private UserController userController;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Test
    void validateUserOk() {
        User user = User.builder()
                .id(1)
                .email("irem.ashfak@mail.ru")
                .login("ashhirka")
                .birthday(LocalDate.parse("05.03.2002", formatter))
                .build();

        assertDoesNotThrow(() -> userController.addUser(user), "Пользователь не создан");
    }

    @Test
    void validateUserFail() {
        User user = User.builder()
                .id(1)
                .email("irem.ashfak@mail.ru")
                .login("irem ashfak")
                .birthday(LocalDate.parse("05.03.2002", formatter))
                .build();

        Exception exception = assertThrows(ValidationException.class, () -> userController.addUser(user));
        assertTrue(exception.getMessage().contains("Логин не может быть пустым и содержать пробелы"));

        user.setLogin("ashhirka");
        LocalDate wrongBirthday = LocalDate.parse("05.03.2025", formatter);
        user.setBirthday(wrongBirthday);

        exception = assertThrows(ValidationException.class, () -> userController.addUser(user));
        assertTrue(exception.getMessage().contains("Дата рождения не может быть в будущем"));
    }
}
