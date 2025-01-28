package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class FilmControllerTest {
    @Test
    void shouldSave() {
        FilmStorage filmStorage = new InMemoryFilmStorage();
        UserStorage userStorage = new InMemoryUserStorage();
        FilmService filmService = new FilmService(filmStorage, userStorage, null);
        FilmController filmController = new FilmController(filmService);
        Film film = Film.builder()
                .name("NameTestOne")
                .description("DescriptionTestOne")
                .releaseDate(LocalDate.of(2008, 1, 1))
                .duration(88888)
                .build();

        FilmDto saveFilm = filmController.create(film);

        assertEquals(FilmMapper.mapToFilmDto(film), saveFilm);
    }

    @Test
    void shouldUpdate() {
        FilmStorage filmStorage = new InMemoryFilmStorage();
        UserStorage userStorage = new InMemoryUserStorage();
        FilmService filmService = new FilmService(filmStorage, userStorage, null);
        FilmController filmController = new FilmController(filmService);
        Film film = Film.builder()
                .name("NameTestTwo")
                .description("DescriptionTestTwo")
                .releaseDate(LocalDate.of(2007, 5, 7))
                .duration(88888)
                .build();
        Film filmNew = Film.builder()
                .name("NameTestTwoNew")
                .description("DescriptionTestTwoNew")
                .releaseDate(LocalDate.of(2008, 6, 8))
                .duration(88888)
                .build();

        FilmDto saveFilm = filmController.create(film);
        Long idFilm = saveFilm.getId();
        filmNew.setId(idFilm);
        FilmDto updateFilm = filmController.create(filmNew);

        assertNotEquals(saveFilm, updateFilm);
    }
}