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
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:schema.sql")
@SpringBootTest
public class LikesDbStorageTest {
    private final UserDbStorage userDBStorage;
    private final FilmDbStorage filmDBStorage;
    private final LikeDbStorage likeDBStorage;

    @Test
    public void getLikesByFilmIdTest() {
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

        User user = User.builder()
                .id(1L)
                .name("TestName")
                .email("testMail@mail.ru")
                .login("TestLogin")
                .birthday(LocalDate.of(2001, 7, 5))
                .friends(new HashSet<>())
                .build();
        userDBStorage.createUser(user);
        likeDBStorage.addLike(1L, 1L);

        User userTwo = User.builder()
                .id(2L)
                .name("TestNameTwo")
                .email("testMailTwo@mail.ru")
                .login("TestLoginTwo")
                .birthday(LocalDate.of(2000, 4, 1))
                .friends(new HashSet<>())
                .build();
        userDBStorage.createUser(userTwo);
        likeDBStorage.addLike(1L, 2L);

        Long[] allArrLikes = {1L, 2L};
        Assertions.assertArrayEquals(allArrLikes, filmDBStorage.getFilmById(1L).getLikes().toArray(),
                "Неверное количество лайков");
    }

    @Test
    public void addLikeByFilmIdTest() {
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

        User user = User.builder()
                .id(1L)
                .name("TestName")
                .email("testMail@mail.ru")
                .login("TestLogin")
                .birthday(LocalDate.of(2001, 7, 5))
                .friends(new HashSet<>())
                .build();
        userDBStorage.createUser(user);
        likeDBStorage.addLike(1L, 1L);
        Long[] arrayLikes = {1L};
        Assertions.assertArrayEquals(arrayLikes, filmDBStorage.getFilmById(1L).getLikes().toArray(), "Лайк к" +
                " фильму не добавился");
    }

    @Test
    public void deleteLikeByFilmIdTest() {
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

        User user = User.builder()
                .id(1L)
                .name("TestName")
                .email("testMail@mail.ru")
                .login("TestLogin")
                .birthday(LocalDate.of(2001, 7, 5))
                .friends(new HashSet<>())
                .build();
        userDBStorage.createUser(user);
        likeDBStorage.addLike(1L, 1L);
        Long[] arrayLikes = {1L};
        Assertions.assertArrayEquals(arrayLikes, filmDBStorage.getFilmById(1L).getLikes().toArray());
        likeDBStorage.deleteLike(1L, 1L);
        Long[] arrayDeletedLikes = {};
        Assertions.assertArrayEquals(arrayDeletedLikes, filmDBStorage.getFilmById(1L).getLikes().toArray(),
                "Не удалился лайк у фильма");
    }
}