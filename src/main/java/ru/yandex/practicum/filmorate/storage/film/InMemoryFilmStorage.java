package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final HashMap<Integer, Film> films = new HashMap<>();
    private int filmId = 1;

    @Override
    public void save(Film film) {
        film.setId(filmId++);
        films.put(film.getId(), film);
    }

    @Override
    public Optional<Film> findById(Integer id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public List<Film> getAll() {
        return films.values().stream().toList();
    }

    @Override
    public void deleteById(Integer id) {
        films.remove(id);
    }

    @Override
    public void update(Film film) {
        films.put(film.getId(), film);
    }

    @Override
    public void like(Integer userId, Integer filmId) {
        films.get(filmId).getLikes().add(userId);
    }

    @Override
    public void unlike(Integer userId, Integer filmId) {
        films.get(filmId)
                .getLikes()
                .remove(userId);
    }

    @Override
    public List<Film> getMostPopular(Integer count) {
        Comparator<Film> comparator = Comparator.comparingInt((f) -> f.getLikes().size());
        comparator = comparator.reversed();
        return getAll().stream().sorted(comparator)
                .limit(count)
                .collect(Collectors.toList());
    }


    public List<Rating> getRating() {
        return List.of();
    }

    public Rating getRating(int id) {
        return null;
    }
}
