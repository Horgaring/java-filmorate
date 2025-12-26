package ru.yandex.practicum.filmorate.exception;

public class ErrorResponse {
    private final String error;
    private String message;

    public ErrorResponse(String error, String message) {
        this.error = error;
    }

}
