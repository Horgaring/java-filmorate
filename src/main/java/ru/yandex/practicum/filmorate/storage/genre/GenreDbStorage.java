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
    private final JdbcTemplate jdbc;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbc = jdbcTemplate;
    }

    public List<Genre> getGenres(Integer filmId) {
        return jdbc.query("SELECT * FROM genres AS g\n" +
                "LEFT JOIN film_genres AS f ON f.genre_id = g.id\n" +
                "WHERE f.film_id = ?", new BeanPropertyRowMapper<>(Genre.class), filmId);
    }

    public List<Genre> getGenres() {
        return jdbc.query("SELECT * FROM genres", new BeanPropertyRowMapper<>(Genre.class));
    }

    public void addGenre(Integer filmId, Genre genre) {
        jdbc.update("INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)",
                filmId,
                genre.getId());
    }


    public Optional<Genre> getGenre(Integer id) {
        try {
            Genre genre = jdbc.queryForObject(
                    "SELECT * FROM genres WHERE id = ?",
                    new BeanPropertyRowMapper<>(Genre.class),
                    id
            );
            return Optional.ofNullable(genre);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}

