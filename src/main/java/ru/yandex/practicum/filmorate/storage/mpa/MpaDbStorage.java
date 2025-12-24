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

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbc = jdbcTemplate;
    }

    public List<Rating> getRating() {
        return jdbc.query("SELECT * FROM mpa\n",
                new BeanPropertyRowMapper<>(Rating.class));
    }

    public Rating getRating(int id) {
        return jdbc.queryForObject("SELECT * FROM mpa AS g\n" +
                "WHERE g.id = ?", new BeanPropertyRowMapper<>(Rating.class), id);
    }

    public Optional<Rating> findById(Integer id) {
        try {
            Rating genre = jdbc.queryForObject(
                    "SELECT * FROM mpa WHERE id = ?",
                    new BeanPropertyRowMapper<>(Rating.class),
                    id
            );
            return Optional.ofNullable(genre);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}

