package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final List<User> users = new ArrayList<>();

    @Override
    public void save(User film) {
        users.add(film);
    }

    @Override
    public Optional<User> findById(Integer id) {
        return users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<User> getAll() {
        return users;
    }

    @Override
    public void deleteById(Integer id) {
        users.removeIf(u -> u.getId().equals(id));
    }

    @Override
    public void update(User user) {
        users.stream()
                .filter(u -> u.getId().equals(user.getId()))
                .findFirst()
                .ifPresent(u -> {
                    u = user;
                });
    }
}
