package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

@Component
public class UserRowMapper implements RowMapper<User> {
    protected final JdbcTemplate jdbc;
    private static final String FIND_ALL_FRIENDS_BY_ID_QUERY = "SELECT friendId FROM friends WHERE userId = ?";

    public UserRowMapper(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getTimestamp("birthday").toLocalDateTime().toLocalDate())
                .friends(findFriendsByUserId(resultSet.getLong("id")))
                .build();
    }

    private Set<Long> findFriendsByUserId(Long id) {
        Set<Long> ids = new HashSet<>();
        SqlRowSet friends = jdbc.queryForRowSet(FIND_ALL_FRIENDS_BY_ID_QUERY, id);
        while (friends.next()) {
            ids.add(friends.getLong("friendid"));
        }
        return ids;
    }
}
