package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@Component
public class GenreDbStorage {
    private static final String SQL_GET_GENRES_BY_FILM_ID =
            "SELECT g.id AS id, g.name AS name " +
                    "FROM genres AS g " +
                    "LEFT JOIN film_genres AS f ON f.genre_id = g.id " +
                    "WHERE f.film_id = ?";
    private static final String SQL_GET_ALL_GENRES =
            "SELECT g.id AS id, g.name AS name FROM genres AS g";
    private static final String SQL_INSERT_FILM_GENRE =
            "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
    private static final String SQL_GET_GENRE_BY_ID =
            "SELECT g.id AS id, g.name AS name FROM genres AS g WHERE g.id = ?";
    private final JdbcTemplate jdbc;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbc = jdbcTemplate;
    }

    public List<Genre> getGenres(Integer filmId) {
        return jdbc.query(
                SQL_GET_GENRES_BY_FILM_ID,
                new BeanPropertyRowMapper<>(Genre.class),
                filmId
        );
    }

    public List<Genre> getGenres() {
        return jdbc.query(
                SQL_GET_ALL_GENRES,
                new BeanPropertyRowMapper<>(Genre.class)
        );
    }

    public void addGenre(Integer filmId, Genre genre) {
        jdbc.update(
                SQL_INSERT_FILM_GENRE,
                filmId,
                genre.getId()
        );
    }

    public Optional<Genre> getGenre(Integer id) {
        try {
            Genre genre = jdbc.queryForObject(
                    SQL_GET_GENRE_BY_ID,
                    new BeanPropertyRowMapper<>(Genre.class),
                    id
            );
            return Optional.ofNullable(genre);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
