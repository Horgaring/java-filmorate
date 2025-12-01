package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;
    private UserStorage userStorage;
    private UserValidator userValidator;
    private int counter = 1;

    public UserController(UserService userService, UserStorage userStorage, UserValidator userValidator) {
        this.userValidator = userValidator;
        this.userService = userService;
        this.userStorage = userStorage;
    }

    @PostMapping
    public User post(@RequestBody User user) {
        if (user.getName() == null || user.getName().isEmpty())
            user.setName(user.getLogin());
        user.setId(this.counter++);
        userValidator.validate(user);
        userStorage.save(user);
        log.info("User {} created", user.getId());
        log.debug("{}", user);
        return user;
    }

    @GetMapping()
    public List<User> get() {
        return userStorage.getAll();
    }

    @PutMapping()
    public User put(@RequestBody User user) {
        log.info("Start Updating user {}", user.getId());
        userValidator.validate(user);
        if (!userStorage.getAll().stream().anyMatch(u -> u.getId() == user.getId())) {
            throw new UserNotFoundException(user.getId());
        }
        userStorage.getAll().stream()
                .filter((s) -> s.getId() == user.getId())
                .forEach((s) -> s = user);
        log.info("User {} updated", user.getId());
        return user;
    }

    @GetMapping("/{id}")
    public User get(@PathVariable Integer id) {
        var user = userStorage.findById(id);
        if (user.isEmpty())
            throw new UserNotFoundException(id);
        return user.get();
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        userService.addToFriendList(id, friendId);
        log.info("Friend {} added to user {}", friendId, id);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        userService.deleteFromFriendList(id, friendId);
        log.info("Friend {} deleted from user {}", friendId, id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable Integer id) {
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherID}")
    public List<User> sharedFriend(@PathVariable Integer id, @PathVariable Integer otherID) {
        return userService.getSharedFriends(id, otherID);
    }
}
