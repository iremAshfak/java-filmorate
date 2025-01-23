package ru.yandex.practicum.filmorate.dto;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class FilmDto {
    Long id;
    String name;
    String description;
    LocalDate releaseDate;
    long duration;
    Set<Long> likes;
    Set<Genre> genres;
    Mpa mpa;
}
