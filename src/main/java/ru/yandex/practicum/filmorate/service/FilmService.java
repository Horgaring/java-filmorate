package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private FilmStorage filmStorage;
    private UserStorage userStorage;

    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void like(Integer userId, Integer filmId) {
        if (userStorage.findById(userId).isEmpty())
            throw new UserNotFoundException(userId);
        filmStorage.findById(filmId)
                .orElseThrow(() -> new FilmNotFoundException(filmId))
                .getLikes()
                .add(userId);
    }

    public void removeLike(Integer userId, Integer filmId) {
        if (userStorage.findById(userId).isEmpty())
            throw new UserNotFoundException(userId);
        filmStorage.findById(filmId)
                .orElseThrow(() -> new FilmNotFoundException(filmId))
                .getLikes()
                .remove(userId);
    }

    public List<Film> getMostPopular(Integer count) {
        Comparator<Film> comparator = Comparator.comparingInt((f) -> f.getLikes().size());
        comparator = comparator.reversed();
        return filmStorage.getAll().stream().sorted(comparator)
                .limit(count)
                .collect(Collectors.toList());
    }

    public void add(Film film) {
        filmStorage.save(film);
    }

    public void deleteFilm(Integer filmId) {
        if (filmStorage.findById(filmId).isEmpty()) {
            throw new FilmNotFoundException(filmId);
        }
        filmStorage.deleteById(filmId);
    }

    public Film findById(Integer filmId) {
        return filmStorage.findById(filmId).orElseThrow(() -> new FilmNotFoundException(filmId));
    }


    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public void update(Film film) {
        if (filmStorage.findById(film.getId()).isEmpty()) {
            throw new FilmNotFoundException(film.getId());
        }
        filmStorage.update(film);
    }
}
