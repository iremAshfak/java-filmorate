package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.List;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film newFilm) {
        return filmStorage.updateFilm(newFilm);
    }

    public void deleteFilm(Integer id) {
        filmStorage.deleteFilm(id);
    }

    public Film addLike(Integer filmId, Integer userId) {
        if (filmStorage.getById(filmId) == null) {
            throw new NotFoundException("Фильм не найден");
        }
        if (filmStorage.getById(userId) == null) {
            throw new NotFoundException("Пользователь не найден");
        }

        filmStorage.getById(filmId).addLike(userId);
        return filmStorage.getById(filmId);
    }

    public Film deleteLike(Integer filmId, Integer userId) {
        if (filmStorage.getById(filmId) == null) {
            throw new NotFoundException("Фильм не найден");
        }
        if (filmStorage.getById(userId) == null) {
            throw new NotFoundException("Пользователь не найден");
        }

        filmStorage.getById(filmId).deleteLike(userId);
        return filmStorage.getById(filmId);
    }

    public List<Film> getBestFilms(Integer count) {
        return filmStorage.getAllFilms().stream()
                .sorted((o1, o2) -> {
                    if (o1.getLikes().size() > o2.getLikes().size()) return -1;
                    else return 1;
                })
                .limit(count)
                .toList();
    }

    public Film getById(Integer id) {
        return filmStorage.getById(id);
    }
}
