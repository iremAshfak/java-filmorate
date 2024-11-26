package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
public class FilmControllerTest {
    @Autowired
    private FilmController filmController;
    private UserController userController;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Test
    void checkFilmIsOk() {
        Film film = Film.builder()
                .id(1L)
                .name("Джокер: Безумие на двоих")
                .description("Американский музыкально-психологический триллер режиссёра Тодда Филлипса")
                .releaseDate(LocalDate.parse("04.09.2024", formatter))
                .duration(138)
                .build();

        assertDoesNotThrow(() -> filmController.create(film), "Фильм не создан");
    }

    @Test
    void checkFilmIsFailed() {
        Film film = Film.builder()
                .id(1L)
                .name("Джокер: Безумие на двоих")
                .description("Американский музыкально-психологический триллер режиссёра Тодда Филлипса")
                .releaseDate(LocalDate.parse("04.09.1824", formatter))
                .duration(138)
                .build();

        Exception exception = assertThrows(ValidationException.class, () -> filmController.create(film));
        assertTrue(exception.getMessage().contains("Дата релиза не может быть ранее 28 декабря 1895 года"));

        film.setReleaseDate(LocalDate.parse("04.09.2024", formatter));
        film.setDescription("Американский музыкально-психологический триллер режиссёра Тодда Филлипса, " +
                "продолжение картины «Джокер». Главную роль в нём исполнил Хоакин Феникс, " +
                "певица Леди Гага сыграла Харли Квинн. Премьера фильма состоялась 4 сентября 2024 года " +
                "на 81-м Венецианском кинофестивале. Находясь на принудительном лечении в больнице Аркхем, " +
                "несостоявшийся комик Артур Флек встречает любовь всей своей жизни — Харли Квинн.");

        exception = assertThrows(ValidationException.class, () -> filmController.create(film));
        assertTrue(exception.getMessage().contains("Описание не может быть длиннее 200 символов"));
    }
}
