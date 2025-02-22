package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.GenreDto;

import ru.yandex.practicum.filmorate.model.Genre;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GenreMapper {
    public static GenreDto mapToGenreDto(Genre genre) {
        if (genre == null) {
            return null;
        }

        GenreDto dto = GenreDto.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();

        return dto;
    }
}
