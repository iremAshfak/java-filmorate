package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.model.Mpa;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MpaMapper {
    public static MpaDto mapToMpaDto(Mpa mpa) {
        if (mpa == null) {
            return null;
        }

        MpaDto dto = MpaDto.builder()
                .id(mpa.getId())
                .name(mpa.getName())
                .build();

        return dto;
    }
}
