package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static final int MAX_LENGTH_DESCRIPTION = 200;
    private static final LocalDate MIN_DATE_RELEASE = LocalDate.parse("1895-12-28");
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Collection<Film> findAllFilms() {
        log.info("Request to get Films");
        return films.values();
    }

    @Override
    public Film createFilm(Film film) {
        validation(film);
        film.setLikesUsers(new HashSet<>());
        log.info("Request to create Film by name {}", film.getName());
        film.setId(getNextId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (film.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }
        validation(film);
        log.info("Request to update Film by id {}", film.getId());
        if (films.containsKey(film.getId())) {
            Film oldFilm = films.get(film.getId());
            oldFilm.setName(film.getName());
            oldFilm.setDuration(film.getDuration());
            oldFilm.setDescription(film.getDescription());
            oldFilm.setReleaseDate(film.getReleaseDate());
            return oldFilm;
        }

        throw new NotFoundException("Фильм с id = " + film.getId() + " не найден");
    }

    @Override
    public Film getFilmById(Long id) {
        if (films.containsKey(id)) {
            return films.get(id);
        }
        throw new NotFoundException("Фильм с id = " + id + " не найден");
    }

    private void validation(Film film) throws ValidationException {
        if (film.getName() == null || film.getName().isEmpty()) {
            log.error("Empty Film name");
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription().length() >= MAX_LENGTH_DESCRIPTION) {
            log.error("Maximum description length is 200 characters");
            throw new ValidationException("Описание не может быть длиннее 200 символов");
        }
        if (film.getReleaseDate().isBefore(MIN_DATE_RELEASE)) {
            log.error("Release date: no earlier than December 28, 1895");
            throw new ValidationException("Дата релиза не может быть ранее 28 декабря 1895 года");
        }
        if (film.getDuration() < 0) {
            log.error("The length of the movie must be a positive number.");
            throw new ValidationException("Продолжительность фильма должна быть положительным числом.");
        }
    }

    private Long getNextId() {
        Long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}