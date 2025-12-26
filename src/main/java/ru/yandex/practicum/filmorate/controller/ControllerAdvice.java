package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.*;

@RestControllerAdvice
@Slf4j
public class ControllerAdvice {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ErrorResponse handleUserNotFound(UserNotFoundException ex) {
        log.warn("User not found: {}", ex.getMessage());
        return new ErrorResponse("User not found", "User " + ex.getUserId() + " not found");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(FilmNotFoundException.class)
    public ErrorResponse handleFilmNotFound(FilmNotFoundException ex) {
        log.warn("Film not found: {}", ex.getMessage());
        return new ErrorResponse("Film not found", "Film " + ex.getFilmId() + " not found");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(MpaNotFoundException.class)
    public ErrorResponse handleMpaNotFound(MpaNotFoundException ex) {
        log.warn("Mpa not found: {}", ex.getMessage());
        return new ErrorResponse("Mpa not found", "Mpa " + ex.getMessage() + " not found");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(GenreNotFoundException.class)
    public ErrorResponse handleMpaNotFound(GenreNotFoundException ex) {
        log.warn("Genre not found: {}", ex.getMessage());
        return new ErrorResponse("Genre not found", "Genre " + ex.getMessage() + " not found");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public ErrorResponse handleFilmNotFound(ValidationException ex) {
        log.warn("Validation Exception: {}", ex.getMessage());
        return new ErrorResponse("Validation Exception", ex.getMessage());
    }
}
