package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    void save(Film film);

    Optional<Film> findById(Integer id);

    List<Film> getAll();

    void deleteById(Integer id);

    void update(Film film);

    void like(Integer userId, Integer filmId);

    void unlike(Integer userId, Integer filmId);

    List<Film> getMostPopular(Integer count);

}
