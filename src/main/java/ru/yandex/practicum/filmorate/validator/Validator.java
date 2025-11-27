package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.exception.ValidationException;

public interface Validator<T> {
    boolean validate(T t) throws ValidationException;
}
