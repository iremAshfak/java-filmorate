package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.HashSet;

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
    HashSet<Integer> likes;

    public void addLike(Integer id) {
        if (this.likes == null) {
            this.likes = new HashSet<>();
        }
        this.likes.add(id);
    }

    public void deleteLike(Integer id) {
        if (this.likes == null) {
            return;
        }
        this.likes.remove(id);
    }
}
