package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addToFriendList(int userId, int friendId) throws UserNotFoundException {
        var user = userStorage.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        var friend = userStorage.findById(friendId).orElseThrow(() -> new UserNotFoundException(friendId));
        userStorage.addFriend(userId, friendId);
        log.info("Added user {} to friend list {}", userId, friendId);
    }

    public void deleteFromFriendList(int userId, int friendId) throws UserNotFoundException {
        var user = userStorage.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        var friend = userStorage.findById(friendId).orElseThrow(() -> new UserNotFoundException(friendId));
        userStorage.removeFriend(userId, friendId);
        log.info("Removed user {} from friend list {}", userId, friendId);
    }

    public List<User> getSharedFriends(int userId, int secondUserId) throws UserNotFoundException {
        log.info("Getting shared friends for users {} and {}", userId, secondUserId);  // Вход
        if (userStorage.findById(userId).isEmpty()) {
            throw new UserNotFoundException(userId);
        }
        if (userStorage.findById(secondUserId).isEmpty()) {
            throw new UserNotFoundException(secondUserId);
        }
        return userStorage.getSharedFriends(userId, secondUserId);
    }

    public List<User> getFriends(int userId) throws UserNotFoundException {
        if (userStorage.findById(userId).isEmpty()) {
            throw new UserNotFoundException(userId);
        }
        var friends = userStorage.getFriendsById(userId);
        log.debug("Friends: {}", friends);
        return friends;
    }

    public void add(User user) {
        userStorage.save(user);
    }

    public void update(User user) {
        if (userStorage.findById(user.getId()).isEmpty()) {
            throw new UserNotFoundException(user.getId());
        }
        userStorage.update(user);
    }

    public void deleteById(Integer userId) {
        userStorage.deleteById(userId);
    }

    public User findById(Integer userId) {
        if (userStorage.findById(userId).isEmpty()) {
            throw new UserNotFoundException(userId);
        }
        return userStorage.findById(userId).get();
    }

    public List<User> findAll() {
        return userStorage.getAll();
    }
}
