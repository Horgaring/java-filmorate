package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Integer, User> users = new HashMap<>();
    private int id = 1;

    @Override
    public void save(User film) {
        film.setId(id++);
        users.put(film.getId(), film);
    }

    @Override
    public Optional<User> findById(Integer id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> getAll() {
        return users.values().stream().toList();
    }

    @Override
    public void deleteById(Integer id) {
        users.remove(id);
    }

    @Override
    public void update(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public void addFriend(int userId, int friendId) {
        var user = users.get(userId);
        var friend = users.get(friendId);
        user.getFriendList().add(new Friendship(userId, friendId, FriendshipStatus.PENDING));
        friend.getFriendList().add(new Friendship(userId, friendId, FriendshipStatus.PENDING));
    }

    @Override
    public void removeFriend(int userId, int friendId) {
        var user = users.get(userId);
        var friend = users.get(friendId);
        user.getFriendList()
                .removeIf((s) -> s.getRequesterId() == userId && s.getAddresseeId() == friendId);
        friend.getFriendList()
                .removeIf((s) -> s.getRequesterId() == userId && s.getAddresseeId() == friendId);
    }

    @Override
    public List<User> getSharedFriends(int userId, int secondUserId) {
        var secondUserFriends = users.get(secondUserId)
                .getFriendList();


        var firstUserFriends = users.get(userId)
                .getFriendList();
        firstUserFriends.retainAll(secondUserFriends);
        List<User> shared = firstUserFriends.stream()
                .map(users::get)
                .collect(Collectors.toList());


        return shared;
    }

    @Override
    public List<User> getFriendsById(int userId) {
        return users.get(userId)
                .getFriendList()
                .stream()
                .filter(s -> s.getStatus() == FriendshipStatus.CONFIRMED)
                .map(s -> {
                    if (s.getRequesterId() == userId) {
                        return users.get(s.getAddresseeId());
                    } else {
                        return users.get(s.getRequesterId());
                    }
                })
                .toList();
    }
}
