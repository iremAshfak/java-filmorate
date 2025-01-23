package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.dal.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbc;
    private final GenreRowMapper mapper;
    private static final String FIND_ALL_QUERY = "SELECT * FROM genres ORDER BY id";
    private static final String FIND_GENRE_BY_ID_QUERY = "SELECT * FROM genres WHERE id = ?";
    private static final String FIND_GENRES_BY_FILM_ID_QUERY = "SELECT g.id, g.name FROM filmgenres fg " +
            "INNER JOIN genres g ON fg.genreid = g.id WHERE fg.filmid = ?";

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate, GenreRowMapper mapper) {
        this.jdbc = jdbcTemplate;
        this.mapper = mapper;
    }

    @Override
    public List<Genre> readAll() {
        log.debug("Получаем все жанры.");
        return jdbc.query(FIND_ALL_QUERY, mapper);
    }

    @Override
    public Genre readById(Long id) {
        if (id == null) {
            throw new ValidationException("Ошибка. Id не может быть null");
        }
        try {
            return jdbc.queryForObject(FIND_GENRE_BY_ID_QUERY, mapper, id);
        } catch (Throwable e) {
            throw new ResponseStatusException(HttpStatus.valueOf(404), "Жанр с данным id не найден.");
        }
    }

    @Override
    public Set<Genre> getGenresByFilmID(Long filmId) {
        List<Genre> genres = jdbc.query(FIND_GENRES_BY_FILM_ID_QUERY, mapper, filmId);
        log.debug("Получаем список жанров фильма с id = {}", filmId);
        return new HashSet<>(genres);
    }
}
