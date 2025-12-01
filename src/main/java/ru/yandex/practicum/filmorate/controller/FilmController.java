package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film post(@RequestBody Film film) {
        FilmValidator.validate(film);
        filmService.add(film);
        log.info("Film {} created", film.getId());
        return film;
    }

    @GetMapping()
    public List<Film> get() {
        return filmService.getAll();
    }

    @PutMapping()
    public Film put(@RequestBody Film film) {
        FilmValidator.validate(film);
        filmService.update(film);
        log.info("Film {} updated", film.getId());
        return film;
    }

    @PutMapping("/{id}/like/{userId}")
    public Film putLike(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.like(userId, id);
        log.info("Film {} liked by user {}", id,  userId);
        return filmService.findById(id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Film {} disliked by user {}", id,  userId);
        filmService.removeLike(userId, id);
    }

    @GetMapping("/{id}")
    public Film get(@PathVariable Integer id) {
        return filmService.findById(id);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(required = false, defaultValue = "10") Integer count) {
        return filmService.getMostPopular(count);
    }
}
