package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.util.List;

@Service
public class GenreService {
    private final GenreDbStorage storage;

    @Autowired
    public GenreService(GenreDbStorage storage) {
        this.storage = storage;
    }


    public List<Genre> getGenres() {
        return storage.getGenres();
    }

    public Genre getGenre(Integer id) {
        return storage.getGenre(id)
                .orElseThrow(() -> new GenreNotFoundException(id));
    }


}
