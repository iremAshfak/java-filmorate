package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    Collection<User> getAllUsers();

    User addUser(User newUser);

    User updateUser(User newUser);

    void deleteUser(Integer id);
    
    User getById(Integer id);
}