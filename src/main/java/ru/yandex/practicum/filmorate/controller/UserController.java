package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User post(@RequestBody User user) {
        if (user.getName() == null || user.getName().isEmpty())
            user.setName(user.getLogin());
        UserValidator.validate(user);
        userService.add(user);
        log.info("User {} created", user.getId());
        log.debug("{}", user);
        return user;
    }

    @GetMapping()
    public List<User> get() {
        return userService.findAll();
    }

    @PutMapping()
    public User put(@RequestBody User user) {
        log.info("Start Updating user {}", user.getId());
        UserValidator.validate(user);
        userService.update(user);
        log.info("User {} updated", user.getId());
        return user;
    }

    @GetMapping("/{id}")
    public User get(@PathVariable Integer id) {
        return userService.findById(id);
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
