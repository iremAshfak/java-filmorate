package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

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
}
