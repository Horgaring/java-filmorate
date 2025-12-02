package ru.yandex.practicum.filmorate.exception;

public class ErrorResponse {
    private String error;
    private String message;

    public ErrorResponse(String error, String message) {
        this.error = error;
    }

}
