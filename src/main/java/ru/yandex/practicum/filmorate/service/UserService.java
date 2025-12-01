package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addToFriendList(int userId, int friendId) throws UserNotFoundException {
        var user = userStorage.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        var friend = userStorage.findById(friendId).orElseThrow(() -> new UserNotFoundException(friendId));
        user.getFriendList().add(friend.getId());
        friend.getFriendList().add(user.getId());
        log.info("Added user {} to friend list {}", userId, friendId);
    }

    public void deleteFromFriendList(int userId, int friendId) throws UserNotFoundException {
        var user = userStorage.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        var friend = userStorage.findById(friendId).orElseThrow(() -> new UserNotFoundException(friendId));
        user.getFriendList().remove(friend.getId());
        friend.getFriendList().remove(user.getId());
        log.info("Removed user {} from friend list {}", userId, friendId);
    }

    public List<User> getSharedFriends(int userId, int secondUserId) throws UserNotFoundException {
        log.info("Getting shared friends for users {} and {}", userId, secondUserId);  // Вход

        var secondUserFriends = userStorage.findById(secondUserId)
                .orElseThrow(() -> {
                    return new UserNotFoundException(secondUserId);
                })
                .getFriendList();

        log.debug("Second user {} has {} friends", secondUserId, secondUserFriends.size());

        var firstUserFriends = userStorage.findById(userId)
                .orElseThrow(() -> {
                    return new UserNotFoundException(userId);
                })
                .getFriendList();
        firstUserFriends.retainAll(secondUserFriends);
        List<User> shared = firstUserFriends.stream()
                .map(s -> userStorage.findById(s).get())
                .collect(Collectors.toList());

        log.info("Found {} shared friends for {} and {}",
                shared.size(), userId, secondUserId);  // Выход
        log.debug("Shared friends: {}", shared);  // Детали (dev)

        return shared;
    }

    public List<User> getFriends(int userId) throws UserNotFoundException {
        var friends =  userStorage.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId))
                .getFriendList()
                .stream()
                .map(s -> userStorage.findById(s).get())
                .toList();
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
