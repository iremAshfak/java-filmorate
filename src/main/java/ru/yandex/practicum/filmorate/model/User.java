package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * User.
 */

@Slf4j
@Data
@Builder
public class User {
    protected int id;
    protected String email;
    protected String login;
    protected String name;
    protected LocalDate birthday;
    Set<Integer> friends;

    public void addFriend(Integer id) {
        if (this.friends == null) {
            this.friends = new HashSet<>();
        }
        this.friends.add(id);
    }

    public void deleteFriend(Integer id) {
        if (this.friends == null) {
            return;
        }
        this.friends.remove(id);
    }
}
