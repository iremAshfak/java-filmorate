package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeStorage likeStorage;
    static LocalDate minReleaseDate = LocalDate.of(1895, 12, 28);

    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage,
                       @Qualifier("UserDbStorage") UserStorage userStorage,
                       LikeStorage likeStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likeStorage = likeStorage;
    }

    public Collection<FilmDto> findAllFilms() {
        log.info("Получение всех фильмов.");
        return filmStorage.findAllFilms()
                .stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    public FilmDto findFilmById(Long filmId) {
        log.info("Получение фильма по id = {} ", filmId);
        return FilmMapper.mapToFilmDto(filmStorage.getFilmById(filmId));
    }


    public FilmDto createFilm(Film film) {
        log.info("Создание фильма: {} ", film);
        validateFilm(film);
        film.setId(getNextId());

        return FilmMapper.mapToFilmDto(filmStorage.createFilm(film));
    }


    public FilmDto updateFilm(Film newFilm) {
        log.info("Обновление фильма с ID: {} ", newFilm.getId());
        if (newFilm.getId() == null) {
            log.error("Ошибка при обновлении: Id должен быть указан");
            throw new ValidationException("Id должен быть указан");
        }
        Film oldFilm = filmStorage.updateFilm(newFilm);
        validateFilm(newFilm);
        oldFilm.setName(newFilm.getName());
        oldFilm.setDescription(newFilm.getDescription());
        oldFilm.setReleaseDate(newFilm.getReleaseDate());
        oldFilm.setDuration(newFilm.getDuration());
        log.info("Фильм с ID: {} успешно обновлен ", newFilm.getId());

        return FilmMapper.mapToFilmDto(oldFilm);
    }


    public void addLikeFilm(Long idFilm, Long idUser) {
        Film film = filmStorage.getFilmById(idFilm);
        userStorage.getUserById(idUser);
        likeStorage.addLike(idFilm, idUser);
        film.getLikes().add(idUser);
        log.info("Лайк у фильма c id " + idFilm + " установлен от пользователя c id " + idUser);
    }


    public void deleteLikeFilm(Long idFilm, Long idUser) {
        Film foundFilm = filmStorage.getFilmById(idFilm);
        userStorage.getUserById(idUser);
        foundFilm.getLikes().remove(idUser);
        log.info("Лайк у фильма c id " + idFilm + " удалён. Oт пользователя c id " + idUser);
    }


    public Collection<FilmDto> getPopularFilms(Long count) {
        if (count <= 0) {
            log.warn("Запрошенное количество фильмов меньше или равно 0. Возвращаем пустую коллекцию.");
            return Collections.emptyList();
        }
        log.info("Получение списка из " + count + " популярных фильмов.");
        Collection<Film> films = filmStorage.findAllFilms();

        if (films.isEmpty()) {
            log.warn("Коллекция фильмов пуста. Возвращаем пустую коллекцию.");
            return Collections.emptyList();
        }

        List<Film> sortedFilms = films.stream()
                .sorted((a, b) -> Integer.compare(b.getLikes().size(), a.getLikes().size()))
                .collect(Collectors.toList());

        log.info("Возвращаемые фильмы: " + sortedFilms.stream()
                .map(film -> film.getId() + " - Likes: " + film.getLikes().size())
                .collect(Collectors.joining(", ")));

        List<FilmDto> popularFilms = sortedFilms.stream()
                .filter(film -> !film.getLikes().isEmpty())
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());

        if (popularFilms.isEmpty()) {
            return sortedFilms.stream()
                    .limit(count)
                    .map(FilmMapper::mapToFilmDto)
                    .collect(Collectors.toList());
        }

        return popularFilms.stream()
                .limit(count)
                .collect(Collectors.toList());
    }

    private static void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Ошибка валидации: Название не может быть пустым");
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            log.error("Ошибка валидации: Максимальная длина описания - 200 символов");
            throw new ValidationException("Максимальная длина описания - 200 символов");
        }
        if (film.getReleaseDate().isBefore(minReleaseDate)) {
            log.error("Ошибка валидации: Дата выпуска не может быть раньше 28.12.1895 года");
            throw new ValidationException("Дата выпуска не может быть раньше 28.12.1895 года");
        }
        if (film.getDuration() < 0) {
            log.error("Ошибка валидации: Длительность фильма не может быть отрицательной");
            throw new ValidationException("Длительность фильма не может быть отрицательной");
        }
    }

    private Long getNextId() {
        Long currentMaxId = (long) Math.toIntExact(filmStorage.findAllFilms().stream()
                .mapToLong(Film::getId)
                .max()
                .orElse(0));
        return ++currentMaxId;
    }
}
