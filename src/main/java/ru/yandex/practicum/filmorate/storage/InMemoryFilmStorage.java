package ru.yandex.practicum.filmorate.storage;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    public Film addFilm(Film film) {
        checkFilm(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film newFilm) {
        if (films.containsKey(newFilm.getId())) {
            films.put(newFilm.getId(), newFilm);
            return newFilm;
        } else {
            throw new NotFoundException("Фильм с id =" + newFilm.getId() + "не найден");
        }
    }

    @Override
    public void deleteFilm(Integer id) {
        films.remove(id);
    }

    @Override
    public Film getById(Integer id) {
        return films.get(id);
    }

    private int getNextId() {
        int currentMaxId = Math.toIntExact(films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0));
        return ++currentMaxId;
    }

    private void checkFilm(Film film) {
        if (film.getName().isEmpty()) {
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Описание не может быть длиннее 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не может быть ранее 28 декабря 1895 года");
        }
        if (film.getDuration() < 0) {
            throw new ValidationException("Продолжительность фильма не может быть отрицательным числом");
        }
    }
}
