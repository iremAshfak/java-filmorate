package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<FilmDto> findAllFilms() {
        return filmService.findAllFilms();
    }

    @GetMapping("{filmId}")
    public FilmDto findFilmById(@PathVariable Long filmId) {
        return filmService.findFilmById(filmId);
    }

    @PostMapping
    public FilmDto create(@RequestBody Film film) {
        return filmService.createFilm(film);
    }

    @PutMapping
    public FilmDto update(@RequestBody Film newFilm) {
        return filmService.updateFilm(newFilm);
    }

    @PutMapping("{id}/like/{userId}")
    public ResponseEntity<?> addLikeFilm(@PathVariable Long id, @PathVariable Long userId) {
        filmService.addLikeFilm(id, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("{id}/like/{userId}")
    public ResponseEntity<?> deleteLikeFilm(@PathVariable Long id, @PathVariable Long userId) {
        filmService.deleteLikeFilm(id, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("popular")
    public Collection<FilmDto> getPopularFilms(@RequestParam(required = false, defaultValue = "10") Long count) {
        return filmService.getPopularFilms(count);
    }
}
