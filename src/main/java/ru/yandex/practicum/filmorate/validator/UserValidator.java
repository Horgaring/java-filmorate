package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
public class UserValidator implements Validator<User> {
    @Override
    public boolean validate(User user) throws ValidationException {
        if (!user.getEmail().contains("@") || user.getEmail().isBlank()) {
            log.warn("Invalid email address");
            throw new ValidationException("Email address is invalid");
        }
        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.warn("Invalid login");
            throw new ValidationException("Login is invalid");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Invalid birthday");
            throw new ValidationException("Birthday is after now");
        }
        return true;
    }
}
