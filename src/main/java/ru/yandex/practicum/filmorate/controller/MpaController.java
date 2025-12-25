package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.RatingService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mpa")
public class MpaController {
    private final RatingService mpaService;

    public MpaController(RatingService mpaService) {
        this.mpaService = mpaService;
    }


    @GetMapping()
    public List<Rating> get() {
        return mpaService.getRating();
    }


    @GetMapping("/{id}")
    public Rating getRatingById(@PathVariable Integer id) {
        return mpaService.getRating(id);
    }


}
