package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final List<Film> films = new ArrayList<>();

    @Override
    public void save(Film film) {
        films.add(film);
    }

    @Override
    public Optional<Film> findById(Integer id) {
        var film = films.stream().filter(f -> f.getId().equals(id)).findFirst();
        if (film.isPresent()) {
            return film;
        }
        return Optional.empty();
    }

    @Override
    public List<Film> getAll() {
        return films;
    }

    @Override
    public void deleteById(Integer id) {
        films.removeIf(f -> f.getId().equals(id));
    }

    @Override
    public void update(Film film) {
        films.stream()
                .filter(f -> f.getId().equals(film.getId()))
                .findFirst()
                .ifPresent(savedFilm -> {
                    savedFilm = film;
                });
    }
}
