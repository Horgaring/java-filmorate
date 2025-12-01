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
        var user = userStorage.findById(userId);
        var friend = userStorage.findById(friendId);
        if (user.isEmpty()) {
            throw new UserNotFoundException(userId);
        }
        if (friend.isEmpty()) {
            throw new UserNotFoundException(friendId);
        }
        user.get().getFriendList().add(friend.get().getId());
        friend.get().getFriendList().add(user.get().getId());
        log.info("Added user {} to friend list {}", userId, friendId);
    }

    public void deleteFromFriendList(int userId, int friendId) throws UserNotFoundException {
        var user = userStorage.findById(userId);
        var friend = userStorage.findById(friendId);
        if (user.isEmpty()) {
            throw new UserNotFoundException(userId);
        }
        if (friend.isEmpty()) {
            throw new UserNotFoundException(friendId);
        }
        user.get().getFriendList().remove(friend.get().getId());
        friend.get().getFriendList().remove(user.get().getId());
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

        List<Integer> firstUserFriends = userStorage.findById(userId)
                .orElseThrow(() -> {

                    return new UserNotFoundException(userId);
                })
                .getFriendList().stream().toList();

        List<User> shared = firstUserFriends.stream()
                .filter(secondUserFriends::contains)
                .distinct()
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
}
