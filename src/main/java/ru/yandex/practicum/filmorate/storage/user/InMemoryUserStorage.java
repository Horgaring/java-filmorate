package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Integer,User> users = new HashMap<>();
    private int id = 1;

    @Override
    public void save(User film) {
        film.setId(id++);
        users.put(film.getId(), film);
    }

    @Override
    public Optional<User> findById(Integer id) {
        if (users.containsKey(id)) {
            return Optional.of(users.get(id));
        }
        return Optional.empty();
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
}
