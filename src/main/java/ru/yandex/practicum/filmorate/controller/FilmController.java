package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private List<Film> films;
    private FilmValidator filmValidator;
    private int counter = 1;

    public FilmController() {
        this.films = new ArrayList<>();
        this.filmValidator = new FilmValidator();
    }

    @PostMapping
    public Film post(@RequestBody Film film) {
        log.info("Posting film: {}", film);
        log.info("Start Creating Film");
        filmValidator.validate(film);
        film.setId(this.counter++);
        this.films.add(film);
        log.info("Film {} created", film.getId());
        return film;
    }

    @GetMapping()
    public List<Film> get() {
        return films;
    }

    @PutMapping()
    public Film put(@RequestBody Film film) {
        log.info("Start updating film {}", film.getId());
        filmValidator.validate(film);
        if (!films.stream().anyMatch(f -> f.getId() == film.getId())) {
            throw new ValidationException("Film id not found");
        }

        films.stream()
                .filter((s) -> s.getId() == film.getId())
                .forEach((s) -> s = film);
        log.info("Film {} updated", film.getId());
        return film;
    }
}
