package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;
import java.util.Optional;

@Component
public class MpaDbStorage {
    private final JdbcTemplate jdbc;

    private static final String SQL_GET_ALL_RATINGS =
            "SELECT g.id AS id, g.name AS name FROM mpa AS g";

    private static final String SQL_GET_RATING_BY_ID =
            "SELECT g.id AS id, g.name AS name FROM mpa AS g WHERE g.id = ?";

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbc = jdbcTemplate;
    }

    public List<Rating> getRating() {
        return jdbc.query(
                SQL_GET_ALL_RATINGS,
                new BeanPropertyRowMapper<>(Rating.class)
        );
    }

    public Rating getRating(int id) {
        return jdbc.queryForObject(
                SQL_GET_RATING_BY_ID,
                new BeanPropertyRowMapper<>(Rating.class),
                id
        );
    }

    public Optional<Rating> findById(Integer id) {
        try {
            Rating genre = jdbc.queryForObject(
                    SQL_GET_RATING_BY_ID,
                    new BeanPropertyRowMapper<>(Rating.class),
                    id
            );
            return Optional.ofNullable(genre);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
