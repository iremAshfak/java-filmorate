package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbc;
    private final FilmRowMapper mapper;
    private final MpaDbStorage mpaDbStorage;
    private final GenreStorage genreStorage;
    private final LikeDbStorage likeDbStorage;
    private static final String FIND_ALL_QUERY = "SELECT * FROM films";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM films WHERE id = ?";
    private static final String INSERT_FILM_ID_AND_GENRE_ID_INTO_FILM_GENRES_QUERY = "INSERT INTO filmgenres " +
            "(filmid,genreid) VALUES (?,?)";
    private static final String UPDATE_QUERY = "UPDATE films SET name = ?, description = ?, releaseDate = ?," +
            " duration = ?, mpaid = ? WHERE id = ?";
    private static final String DELETE_INTO_FILM_GENRES_QUERY_BY_FILM_ID = "DELETE FROM filmgenres WHERE filmid = ?";
    private static final String UPDATE_BY_FILM_ID_AND_GENRE_ID_QUERY = "INSERT INTO filmgenres (filmid, genreid) " +
            "VALUES (?,?)";
    private static final String FIND_TOP_FILMS_QUERY = "SELECT id, name, description, releaseDate, duration, mpaid" +
            " FROM films" +
            " LEFT JOIN likes ON id = filmid " +
            "GROUP BY id ORDER BY count(userid) desc  " +
            "LIMIT ?";

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate,
                         FilmRowMapper mapper,
                         MpaDbStorage mpaDbStorage,
                         GenreStorage genreStorage,
                         LikeDbStorage likeDbStorage) {
        this.jdbc = jdbcTemplate;
        this.mapper = mapper;
        this.mpaDbStorage = mpaDbStorage;
        this.genreStorage = genreStorage;
        this.likeDbStorage = likeDbStorage;
    }

    @Override
    public Collection<Film> findAllFilms() {
        List<Film> films = jdbc.query(FIND_ALL_QUERY, mapper);
        log.debug("Получаем список всех фильмов.");

        return films;
    }

    @Override
    public Film getFilmById(Long id) {
        try {
            Film film = jdbc.queryForObject(FIND_BY_ID_QUERY, mapper, id);
            log.debug("Получен фильм с id = {}.", id);

            return film;
        } catch (Throwable throwable) {
            throw new NotFoundException("Фильм с данным id не найден.");
        }
    }

    @Override
    public Film createFilm(Film film) {
        List<Mpa> allMpa = mpaDbStorage.readAll();
        boolean mpaExists = allMpa.stream().anyMatch(mpa -> mpa.getId().equals(film.getMpa().getId()));

        if (!mpaExists) {
            log.error("Ошибка валидации: MPA не может быть пустым или иметь null id, или не существует в" +
                    " базе данных");
            throw new ValidationException("MPA не может быть пустым, иметь null id или не существовать в" +
                    " базе данных");
        }
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbc).withTableName("films")
                .usingGeneratedKeyColumns("id");
        Number key = simpleJdbcInsert.executeAndReturnKey(film.filmToMap());
        film.setId((Long) key);
        film.setMpa(mpaDbStorage.readById(film.getMpa().getId()));

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            List<Genre> availableGenres = genreStorage.readAll();
            Set<Genre> filmGenres = film.getGenres();

            for (Genre genre : filmGenres) {
                boolean genreExists = availableGenres.stream().anyMatch(genreG -> genreG.getId().equals(genre.getId()));
                if (!genreExists) {
                    throw new ValidationException("Жанр с id " + genre.getId() + " не существует в базе данных");
                }
            }

            for (Genre genre : film.getGenres()) {
                jdbc.update(INSERT_FILM_ID_AND_GENRE_ID_INTO_FILM_GENRES_QUERY, film.getId(), genre.getId());
            }
        }
        log.debug("Сохранен фильм c id = {}.", film.getId());

        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (film == null) {
            throw new ValidationException("Фильм не найден");
        }

        if (jdbc.update(UPDATE_QUERY, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getMpa().getId(), film.getId()) != 0) {
            log.info("Обновлен фильм c id = {}", film.getId());
        } else {
            throw new NotFoundException("Фильм с данным id не найден");
        }

        if (film.getGenres() == null || film.getGenres().isEmpty()) {
            jdbc.update(DELETE_INTO_FILM_GENRES_QUERY_BY_FILM_ID, film.getId());
        } else {
            jdbc.update(DELETE_INTO_FILM_GENRES_QUERY_BY_FILM_ID, film.getId());
            for (Genre genre : film.getGenres()) {
                jdbc.update(UPDATE_BY_FILM_ID_AND_GENRE_ID_QUERY, film.getId(), genre.getId());
            }
        }
        film.setGenres(genreStorage.getGenresByFilmID(film.getId()));
        System.out.println("film");

        return film;
    }

    @Override
    public Set<Film> getTopFilms(Long count) {
        List<Film> topFilms = jdbc.query(FIND_TOP_FILMS_QUERY, mapper, count);
        log.debug("Получаем топ count = {} фильмов по количеству лайков.", count);

        return new HashSet<>(topFilms);
    }
}
