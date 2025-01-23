package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dal.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:schema.sql")
@SpringBootTest
public class FilmDbStorageTest {
    private final FilmDbStorage filmDBStorage;

    @Test
    public void getAllFilmsTest() {
        Film film = Film.builder()
                .id(1L)
                .name("TestName")
                .description("TestDescription")
                .releaseDate(LocalDate.of(2005, 7, 1))
                .duration(100)
                .genres(Set.of(new Genre(3L, "Мультфильм")))
                .likes(new HashSet<>())
                .mpa(new Mpa(4L, "PG-13"))
                .build();
        Film filmTwo = Film.builder()
                .id(2L)
                .name("TestNameTwo")
                .description("TestDescriptionTwo")
                .releaseDate(LocalDate.of(2006, 8, 10))
                .duration(120)
                .genres(Set.of(new Genre(1L, "Комедия")))
                .likes(new HashSet<>())
                .mpa(new Mpa(4L, "PG-13"))
                .build();
        filmDBStorage.createFilm(film);
        filmDBStorage.createFilm(filmTwo);
        Assertions.assertEquals(2, filmDBStorage.findAllFilms().size(), "Размер списка фильмов " +
                "не соответствует количеству добавленных фильмов.");
    }

    @Test
    public void getFilmByIdTest() {
        Film film = Film.builder()
                .id(1L)
                .name("TestName")
                .description("TestDescription")
                .releaseDate(LocalDate.of(2005, 7, 1))
                .duration(100)
                .genres(Set.of(new Genre(3L, "Мультфильм")))
                .likes(new HashSet<>())
                .mpa(new Mpa(4L, "PG-13"))
                .build();
        filmDBStorage.createFilm(film);
        Assertions.assertEquals(film, filmDBStorage.getFilmById(1L), "Не нашли фильм по id");
    }

    @Test
    public void saveFilmTest() {
        Film film = Film.builder()
                .id(1L)
                .name("TestName")
                .description("TestDescription")
                .releaseDate(LocalDate.of(2005, 7, 1))
                .duration(100)
                .genres(Set.of(new Genre(3L, "Мультфильм")))
                .likes(new HashSet<>())
                .mpa(new Mpa(4L, "PG-13"))
                .build();
        filmDBStorage.createFilm(film);
        Assertions.assertEquals(film, filmDBStorage.getFilmById(1L), "Не получилось создать фильм.");
    }

    @Test
    public void updateFilmTest() {
        Film film = Film.builder()
                .id(1L)
                .name("TestName")
                .description("TestDescription")
                .releaseDate(LocalDate.of(2005, 7, 1))
                .duration(100)
                .genres(Set.of(new Genre(3L, "Мультфильм")))
                .likes(new HashSet<>())
                .mpa(new Mpa(4L, "PG-13"))
                .build();
        filmDBStorage.createFilm(film);
        film.setName("newTestName");
        filmDBStorage.updateFilm(film);
        Assertions.assertEquals(film, filmDBStorage.getFilmById(1L), "Не получилось обновить фильм.");
    }
}
