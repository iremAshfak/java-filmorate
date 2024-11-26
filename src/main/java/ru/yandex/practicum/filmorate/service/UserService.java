package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public Collection<User> findAllUsers() {
        return userStorage.findAllUsers();
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public User addFriend(Long userId, Long friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        user.getFriends().add(friend.getId());
        friend.getFriends().add(user.getId());
        return user;
    }

    public User removeFriend(Long userId, Long friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        user.getFriends().remove(friend.getId());
        friend.getFriends().remove(user.getId());
        return user;
    }

    public List<User> getFriends(Long userId) {
        User user = userStorage.getUserById(userId);
        return user.getFriends().stream().map(userStorage::getUserById).toList();
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        Set<Long> commonFriends = userStorage.getUserById(user.getId()).getFriends();
        List<User> commonFriendsSet = new ArrayList<>();
        commonFriends.retainAll(userStorage.getUserById(friend.getId()).getFriends());
        for (Long commonFriend : commonFriends) {
            commonFriendsSet.add(userStorage.getUserById(commonFriend));
        }
        return commonFriendsSet;
    }
}