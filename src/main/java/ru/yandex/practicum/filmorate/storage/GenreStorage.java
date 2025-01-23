package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface GenreStorage {
    List<Genre> readAll();

    Genre readById(Long id);

    Set<Genre> getGenresByFilmID(Long filmId);

}