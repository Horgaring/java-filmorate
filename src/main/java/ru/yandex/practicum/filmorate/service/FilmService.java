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
        if (count == null)
            count = 10;
        Comparator<Film> comparator = Comparator.comparingInt((f) -> f.getLikes().size());
        comparator = comparator.reversed();
        return filmStorage.getAll().stream().sorted(comparator)
                .limit(count)
                .collect(Collectors.toList());
    }
}
