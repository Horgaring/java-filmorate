package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    void save(User film);

    Optional<User> findById(Integer id);

    List<User> getAll();

    void deleteById(Integer id);

    void update(User user);

    void addFriend(int userId, int friendId);

    void removeFriend(int userId, int friendId);

    List<User> getSharedFriends(int userId, int secondUserId);

    List<User> getFriendsById(int userId);
}
