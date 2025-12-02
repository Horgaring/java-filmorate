package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final HashMap<Integer,Film> films = new HashMap<>();
    private int filmId = 1;

    @Override
    public void save(Film film) {
        film.setId(filmId++);
        films.put(film.getId(),film);
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
}
