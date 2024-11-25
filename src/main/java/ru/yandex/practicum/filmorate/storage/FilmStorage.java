package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Collection<Film> getAllFilms();

    Film addFilm(Film newFilm);

    Film updateFilm(Film newFilm);

    void deleteFilm(Integer id);

    Film getById(Integer id);
}