package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private FilmService filmService;
    private FilmStorage filmStorage;
    private FilmValidator filmValidator;
    private int counter = 1;

    public FilmController(FilmValidator validator,  FilmService filmService, FilmStorage filmStorage) {
        this.filmValidator = validator;
        this.filmService = filmService;
        this.filmStorage = filmStorage;
    }

    @PostMapping
    public Film post(@RequestBody Film film) {
        filmValidator.validate(film);
        film.setId(this.counter++);
        filmStorage.save(film);
        log.info("Film {} created", film.getId());
        return film;
    }

    @GetMapping()
    public List<Film> get() {
        return filmStorage.getAll();
    }

    @PutMapping()
    public Film put(@RequestBody Film film) {
        filmValidator.validate(film);
        if (filmStorage.findById(film.getId()).isEmpty()) {
            throw new FilmNotFoundException(film.getId());
        }

        filmStorage.getAll().stream()
                .filter((s) -> s.getId() == film.getId())
                .forEach((s) -> s = film);
        log.info("Film {} updated", film.getId());
        return film;
    }

    @PutMapping("/{id}/like/{userId}")
    public Film putLike(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.like(userId, id);
        log.info("Film {} liked by user {}", id,  userId);
        return filmStorage.findById(id).get();
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Film {} disliked by user {}", id,  userId);
        filmService.removeLike(userId, id);
    }

    @GetMapping("/{id}")
    public Film get(@PathVariable Integer id) {
        var film = filmStorage.findById(id);
        if (film.isEmpty())
            throw new UserNotFoundException(id);
        return film.get();
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(required = false) Integer count) {
        return filmService.getMostPopular(count);
    }
}
