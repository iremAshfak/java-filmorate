package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User updateUser(User newUser) {
        return userStorage.updateUser(newUser);
    }

    public void deleteUser(Integer id) {
        userStorage.deleteUser(id);
    }

    public List<User> getUserFriends(Integer userId) {
        if (userStorage.getById(userId) == null) {
            throw new NotFoundException("Пользователь не найден");
        }

        Set<Integer> friendsList = userStorage.getById(userId).getFriends();
        if (friendsList == null) {
            return Collections.emptyList();
        }

        return userStorage.getAllUsers().stream()
                .filter(user -> friendsList.contains(user.getId()))
                .toList();
    }

    public User addToFriends(Integer userId, Integer friendId) {
        if (userStorage.getById(userId) == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        if (userStorage.getById(friendId) == null) {
            throw new NotFoundException("Пользователь-друг не найден");
        }
        if (userId.equals(friendId)) {
            throw new ValidationException("Пользователь не может добавить в друзья сам себя");
        }

        userStorage.getById(userId).addFriend(friendId);
        userStorage.getById(friendId).addFriend(userId);

        return userStorage.getById(userId);
    }

    public User deleteFromFriends(Integer userId, Integer deletedFriendId) {
        if (userStorage.getById(userId) == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        if (userStorage.getById(deletedFriendId) == null) {
            throw new NotFoundException("Пользователь-друг не найден");
        }

        userStorage.getById(userId).deleteFriend(deletedFriendId);
        userStorage.getById(deletedFriendId).deleteFriend(userId);

        return userStorage.getById(userId);
    }

    public List<User> getCommonFriends(Integer userId, Integer otherId) {
        if (userStorage.getById(userId) == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        if (userStorage.getById(otherId) == null) {
            throw new NotFoundException("Другой пользователь не найден");
        }

        Set<Integer> userFriendsList = userStorage.getById(userId).getFriends();
        Set<Integer> otherFriendsList = userStorage.getById(otherId).getFriends();
        if (userFriendsList == null || otherFriendsList == null) {
            return Collections.emptyList();
        }
        userFriendsList.retainAll(otherFriendsList);
        return userStorage.getAllUsers().stream()
                .filter(user -> userFriendsList.contains(user.getId()))
                .toList();
    }

    public User getById(Integer id) {
        return userStorage.getById(id);
    }
}
