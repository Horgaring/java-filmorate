package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.FilmValidator;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.time.LocalDate;

@SpringBootTest
class FilmorateApplicationTests {


    @Test
    void shouldThrowValidationExceptionWithFilmIsNull() {
        var validator = new FilmValidator();
        Assertions.assertThrows(ValidationException.class, () -> validator.validate(null));
    }

    @Test
    void shouldThrowValidationExceptionWhenFilmNameIsEmpty() {
        var validator = new FilmValidator();
        var film = Film.builder()
                .name("")
                .description("a".repeat(199))
                .releaseDate(LocalDate.now())
                .duration(100)
                .build();
        Assertions.assertThrows(ValidationException.class, () -> validator.validate(film));
    }

    @Test
    void shouldThrowValidationExceptionWhenFilmDescriptionMore200() {
        var validator = new FilmValidator();
        var film = Film.builder()
                .name("1")
                .description("a".repeat(201))
                .releaseDate(LocalDate.now())
                .duration(100)
                .build();
        Assertions.assertThrows(ValidationException.class, () -> validator.validate(film));
    }

    @Test
    void shouldReturnTrueWhenFilmDescriptionMore200() {
        var validator = new FilmValidator();
        var film = Film.builder()
                .name("1")
                .description("a".repeat(199))
                .releaseDate(LocalDate.now())
                .duration(100)
                .build();
        Assertions.assertTrue(validator.validate(film));
    }

    @Test
    void shouldReturnTrueWhenFilmReleaseDateAfter1895() {
        var validator = new FilmValidator();
        var film = Film.builder()
                .name("1")
                .description("a")
                .releaseDate(LocalDate.now())
                .duration(100)
                .build();
        Assertions.assertTrue(validator.validate(film));
    }

    @Test
    void shouldThrowValidationExceptionWhenFilmReleaseDateBefore1895() {
        var validator = new FilmValidator();
        var film = Film.builder()
                .name("1")
                .description("a")
                .releaseDate(LocalDate.of(1890, 1, 1))
                .duration(100)
                .build();
        Assertions.assertThrows(ValidationException.class, () -> validator.validate(film));
    }

    @Test
    void shouldReturnTrueWhenUserEmailIsCorrect() {
        var validator = new UserValidator();
        var user = User.builder()
                .name("1")
                .email("a@a")
                .login("a")
                .birthday(LocalDate.now())
                .build();
        Assertions.assertTrue(validator.validate(user));
    }

    @Test
    void shouldThrowValidationExceptionWhenUserEmailIsIncorrect() {
        var validator = new UserValidator();
        var user = User.builder()
                .name("1")
                .email(" ")
                .login("a")
                .birthday(LocalDate.now())
                .build();
        Assertions.assertThrows(ValidationException.class, () -> validator.validate(user));
    }

    @Test
    void shouldReturnTrueWhenUserLoginIsCorrect() {
        var validator = new UserValidator();
        var user = User.builder()
                .name("1")
                .email("s@")
                .login("a")
                .birthday(LocalDate.now())
                .build();
        Assertions.assertTrue(validator.validate(user));
    }

    @Test
    void shouldThrowValidationExceptionWhenUserLoginIsIncorrect() {
        var validator = new UserValidator();
        var user = User.builder()
                .name("1")
                .email("s@")
                .login(" ")
                .birthday(LocalDate.now())
                .build();
        Assertions.assertThrows(ValidationException.class, () -> validator.validate(user));
    }

    @Test
    void shouldReturnTrueWhenUserBirthdayBeforeNow() {
        var validator = new UserValidator();
        var user = User.builder()
                .name("1")
                .email("s@")
                .login("a")
                .birthday(LocalDate.now())
                .build();
        Assertions.assertTrue(validator.validate(user));
    }

    @Test
    void shouldThrowValidationExceptionWhenUserBirthdayAfterNow() {
        var validator = new UserValidator();
        var user = User.builder()
                .name("1")
                .email("s@")
                .login("shshj")
                .birthday(LocalDate.now().plusDays(1))
                .build();
        Assertions.assertThrows(ValidationException.class, () -> validator.validate(user));
    }
}
