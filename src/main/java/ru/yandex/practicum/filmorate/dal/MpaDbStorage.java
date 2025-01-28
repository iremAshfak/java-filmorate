package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.dal.mappers.MpaRowMapper;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Slf4j
@Component
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbc;
    private final MpaRowMapper mapper;
    private static final String FIND_ALL_QUERY = "SELECT * FROM mpa ORDER BY id";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM mpa WHERE id = ?";

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbc, MpaRowMapper mapper) {
        this.jdbc = jdbc;
        this.mapper = mapper;
    }

    @Override
    public List<Mpa> readAll() {
        log.debug("Получаем список Mpa.");
        return jdbc.query(FIND_ALL_QUERY, mapper);
    }

    @Override
    public Mpa readById(Long id) {
        if (id == null) {
            throw new ValidationException("Ошибка. Id не может быть null.");
        }

        try {
            log.debug("Получаем Mpa по id = {}.", id);
            return jdbc.queryForObject(FIND_BY_ID_QUERY, mapper, id);
        } catch (Throwable e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Данный Mpa не найден.");
        }
    }
}
