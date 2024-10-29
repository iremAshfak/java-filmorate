package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        checkUser(user);
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User newUser) {
        if (users.containsKey(newUser.getId())) {
            users.put(newUser.getId(), newUser);
            return newUser;
        } else {
            throw new NotFoundException("Фильм с id =" + newUser.getId() + "не найден");
        }
    }

    private int getNextId() {
        int currentMaxId = Math.toIntExact(users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0));
        return ++currentMaxId;
    }

    private void checkUser(User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new ValidationException("Email не может быть пустым и должен содержать @");
        }
        if (user.getLogin() == null || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}

