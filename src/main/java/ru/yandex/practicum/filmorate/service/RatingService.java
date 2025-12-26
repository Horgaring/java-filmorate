package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import java.util.List;

@Service
public class RatingService {
    private MpaDbStorage mpaStorage;

    public RatingService(MpaDbStorage mpaDbStorage) {
        this.mpaStorage = mpaDbStorage;
    }

    public Rating getRating(Integer id) {
        if (mpaStorage.findById(id).isEmpty()) {
            throw new MpaNotFoundException(id);
        }
        return mpaStorage.getRating(id);
    }

    public List<Rating> getRating() {
        return mpaStorage.getRating();
    }
}
