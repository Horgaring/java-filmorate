package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Slf4j
public class FilmValidator {

    public static boolean validate(Film film) throws ValidationException {
        if (film == null) {
            log.warn("Film is null");
            throw new ValidationException("Film is null");
        }
        if (film.getName().isBlank()) {
            log.warn("Film name is blank");
            throw new ValidationException("Film name is blank");
        }
        if (film.getDescription().length() > 200) {
            log.warn("Film description is too long");
            throw new ValidationException("Film description is too long");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Film release date is before 1985");
            throw new ValidationException("Film release date is before 1985");
        }
        if (film.getDuration() < 0) {
            log.warn("Film duration is negative");
            throw new ValidationException("Film duration is negative");
        }
        return true;
    }
}
