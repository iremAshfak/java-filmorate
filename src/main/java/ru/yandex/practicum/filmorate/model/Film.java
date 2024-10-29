package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

/**
 * Film.
 */

@Slf4j
@Data
@Builder
public class Film {
    protected int id;
    protected String name;
    protected String description;
    protected LocalDate releaseDate;
    protected Integer duration;
}
