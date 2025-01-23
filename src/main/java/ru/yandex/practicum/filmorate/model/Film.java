package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Film.
 */
@Data
@Builder
public class Film {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Set<Long> likesUsers;

    Set<Long> likes = new HashSet<>();
    Set<Genre> genres = new HashSet<>();
    Mpa mpa;

    public Map<String, Object> filmToMap() {
        Map<String, Object> temp = new HashMap<>();
        temp.put("name", name);
        temp.put("description", description);
        temp.put("releaseDate", releaseDate);
        temp.put("duration", duration);
        temp.put("mpaid", mpa.getId());
        return temp;
    }
}
