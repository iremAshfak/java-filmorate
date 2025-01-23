package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Slf4j
@Component("UserDbStorage")
public class UserDbStorage implements UserStorage {
    protected final JdbcTemplate jdbc;
    private final UserRowMapper mapper;
    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String UPDATE_QUERY = "UPDATE users SET email = ?, login = ?, name =?, birthday = ? " +
            "WHERE id = ?";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String DELETE_INTO_FRIENDS_QUERY = "DELETE FROM friends WHERE userid = ? AND friendid = ?";
    private static final String FIND_ALL_FRIENDS_BY_ID_QUERY = "SELECT * FROM users WHERE id IN (SELECT friendid " +
            "FROM friends WHERE userid = ?)";
    private static final String INSERT_FRIEND_BY_ID_QUERY = "INSERT INTO friends (userid, friendid) VALUES (?,?)";

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate, UserRowMapper mapper) {
        this.jdbc = jdbcTemplate;
        this.mapper = mapper;
    }

    @Override
    public List<User> findAllUsers() {
        log.debug("Получаем всех пользователей");
        return jdbc.query(FIND_ALL_QUERY, mapper);
    }

    @Override
    public User getUserById(Long id) {
        try {
            User user = jdbc.queryForObject(FIND_BY_ID_QUERY, mapper, id);
            log.debug("Получен пользователь с id = {}.", id);
            return user;
        } catch (Throwable throwable) {
            throw new NotFoundException("Пользователь с данным id не найден");
        }
    }

    @Override
    public User createUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbc).withTableName("users")
                .usingGeneratedKeyColumns("id");
        Number key = simpleJdbcInsert.executeAndReturnKey(user.userToMap());
        user.setId((Long) key);
        log.debug("Создан пользователь с id = {}.", user.getId());
        return user;
    }

    @Override
    public User updateUser(User newUser) {
        if (newUser == null || newUser.getId() == null) {
            throw new ValidationException("Невалидный пользователь");
        }
        if (jdbc.update(UPDATE_QUERY, newUser.getEmail(), newUser.getLogin(), newUser.getName(), newUser.getBirthday(),
                newUser.getId()) != 0) {
            log.debug(" Пользователь с id = {} успешно обновлён", newUser.getId());
            return newUser;
        } else {
            log.error("Ошибка при обновлении. Пользователь с id = {} не найден", newUser.getId());
            throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
        }
    }

    @Override
    public void userAddFriend(Long userId, Long friendId) {
        getUserById(userId);
        getUserById(friendId);
        try {
            jdbc.update(INSERT_FRIEND_BY_ID_QUERY, userId, friendId);
            log.debug("Друг с id = {} добавлен в друзья пользователю c id = {}.", friendId, userId);
        } catch (DuplicateKeyException e) {
            log.debug("Ошибка при добавлении. Друг с id = {} не добавлен в друзья пользователю c id = {}.", friendId, userId);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public void userDeleteFriend(Long userId, Long friendId) {
        getUserById(userId);
        getUserById(friendId);
        try {
            jdbc.update(DELETE_INTO_FRIENDS_QUERY, userId, friendId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь с данным id отсутствует.");
        }
    }

    @Override
    public List<User> getAllFriendByUserId(Long id) {
        getUserById(id);
        getUserById(id);
        List<User> friends = jdbc.query(FIND_ALL_FRIENDS_BY_ID_QUERY, mapper, id);
        log.debug("Получен список  друзей у пользователя с id {}.", id);
        return friends;
    }
}
