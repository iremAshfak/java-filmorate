package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class User {
    Long id;
    String email;
    String login;
    String name;
    LocalDate birthday;
    private Set<Long> friends = new HashSet<>();

    public Map<String, Object> userToMap() {
        Map<String, Object> temp = new HashMap<>();
        temp.put("email", email);
        temp.put("login", login);
        temp.put("name", name);
        temp.put("birthday", birthday);
        return temp;
    }
}
