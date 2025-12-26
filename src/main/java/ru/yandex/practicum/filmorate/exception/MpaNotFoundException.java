package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class MpaNotFoundException extends RuntimeException {
    private final Integer mpaId;

    public MpaNotFoundException(Integer mpa) {
        this.mpaId = mpa;
    }
}
