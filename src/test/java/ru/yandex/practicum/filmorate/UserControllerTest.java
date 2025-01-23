package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class UserControllerTest {

    @Test
    void shouldSave() {
        User user = User.builder()
                .email("testOne@mail.ru")
                .login("loginTestOne")
                .name("NameTestOne")
                .birthday(LocalDate.of(1999, 7, 4))
                .build();
        FilmStorage filmStorage = new InMemoryFilmStorage();
        UserStorage userStorage = new InMemoryUserStorage();
        UserService userService = new UserService(filmStorage, userStorage);
        UserController userController = new UserController(userService);
        UserDto saveUser = userController.create(user);

        assertEquals(UserMapper.mapToUserDto(user), saveUser);
    }

    @Test
    void shouldUpdate() {
        FilmStorage filmStorage = new InMemoryFilmStorage();
        UserStorage userStorage = new InMemoryUserStorage();
        UserService userService = new UserService(filmStorage, userStorage);
        UserController userController = new UserController(userService);
        User user = User.builder()
                .email("testTwo@mail.ru")
                .login("loginTestTwo")
                .name("NameTestTwo")
                .birthday(LocalDate.of(2000, 7, 5))
                .build();
        User userNew = User.builder()
                .email("testTwoNew@mail.ru")
                .login("loginTestTwoNew")
                .name("NameTestTwoNew")
                .birthday(LocalDate.of(2001, 8, 6))
                .build();

        UserDto saveUser = userController.create(user);
        Long idUser = saveUser.getId();
        userNew.setId(idUser);
        UserDto updateUser = userController.update(userNew);

        assertNotEquals(saveUser, updateUser);
    }
}