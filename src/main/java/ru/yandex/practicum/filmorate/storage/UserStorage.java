package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserStorage {
    Collection<User> findAllUsers();

    User getUserById(Long id);

    User createUser(User user);

    User updateUser(User user);

    void userAddFriend(Long userId, Long friendId);

    void userDeleteFriend(Long userId, Long friendId);

    public List<User> getAllFriendByUserId(Long id);
}
