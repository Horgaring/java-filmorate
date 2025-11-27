package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private List<User> users;
    private UserValidator userValidator;
    private int counter = 1;

    public UserController() {
        this.users = new ArrayList<>();
        this.userValidator = new UserValidator();
    }

    @PostMapping
    public User post(@RequestBody User user) {
        log.info("Start Creating User");
        if (user.getName() == null || user.getName().isEmpty())
            user.setName(user.getLogin());
        user.setId(this.counter++);
        userValidator.validate(user);
        this.users.add(user);
        log.info("User {} created", user.getId());
        return user;
    }

    @GetMapping()
    public List<User> get() {
        return users;
    }

    @PutMapping()
    public User put(@RequestBody User user) {
        log.info("Start Updating user {}", user.getId());
        userValidator.validate(user);
        if (!users.stream().anyMatch(u -> u.getId() == user.getId())) {
            throw new ValidationException("Film id not found");
        }
        users.stream()
                .filter((s) -> s.getId() == user.getId())
                .forEach((s) -> s = user);
        log.info("User {} updated", user.getId());
        return user;
    }
}
