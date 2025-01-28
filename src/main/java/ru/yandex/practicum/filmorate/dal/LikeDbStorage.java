package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.mappers.LikesRowMapper;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class LikeDbStorage implements LikeStorage {
    private final JdbcTemplate jdbc;
    private final LikesRowMapper mapper;
    private static final String FIND_BY_ID_QUERY = "SELECT userid FROM likes WHERE filmid = ?";
    private static final String DELETE_INTO_LIKES_QUERY = "DELETE FROM likes WHERE filmid = ? AND userid = ?";
    private static final String INSERT_LIKE_BY_ID_QUERY = "INSERT INTO likes (filmid, userid) VALUES  (?,?) ";

    @Autowired
    public LikeDbStorage(JdbcTemplate jdbc, LikesRowMapper mapper) {
        this.jdbc = jdbc;
        this.mapper = mapper;
    }

    @Override
    public Set<Long> getLikerByFilmId(Long filmId) {
        List<Long> likes = jdbc.query(FIND_BY_ID_QUERY, mapper, filmId);
        log.debug("Получаем список лайков у фильма с id = {}.", filmId);
        return new HashSet<>(likes);
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        jdbc.update(INSERT_LIKE_BY_ID_QUERY, filmId, userId);
        log.debug("Фильм с id = {} получил лайк от пользователя с id = {}.", filmId, userId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        jdbc.update(DELETE_INTO_LIKES_QUERY, filmId, userId);
        log.debug("Лайк у фильма с id = {} от пользователя с id = {} удалён.", filmId, userId);
    }
}
