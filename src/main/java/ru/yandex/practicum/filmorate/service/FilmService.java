package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, @Qualifier("userDbStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void like(Integer userId, Integer filmId) {
        if (userStorage.findById(userId).isEmpty())
            throw new UserNotFoundException(userId);
        if (filmStorage.findById(filmId).isEmpty())
            throw new FilmNotFoundException(filmId);
        filmStorage.like(userId, filmId);
    }

    public void removeLike(Integer userId, Integer filmId) {
        if (userStorage.findById(userId).isEmpty())
            throw new UserNotFoundException(userId);
        if (filmStorage.findById(filmId).isEmpty())
            throw new FilmNotFoundException(filmId);
        filmStorage.unlike(userId, filmId);
    }

    public List<Film> getMostPopular(Integer count) {
        return filmStorage.getMostPopular(count);
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
