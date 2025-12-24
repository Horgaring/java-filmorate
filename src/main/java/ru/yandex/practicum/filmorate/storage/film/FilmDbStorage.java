package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import java.sql.*;
import java.sql.Date;
import java.util.*;

@Component("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbc;
    private final GenreDbStorage genreDbStorage;
    private final MpaDbStorage mpaDbStorage;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreDbStorage genreDbStorage, MpaDbStorage mpaDbStorage) {
        this.jdbc = jdbcTemplate;
        this.genreDbStorage = genreDbStorage;
        this.mpaDbStorage = mpaDbStorage;
    }

    @Override
    public void save(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();



        if (film.getMpa() == null) {
            jdbc.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO films (name, description, release_date, duration) VALUES (?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS
                );
                ps.setString(1, film.getName());
                ps.setString(2, film.getDescription());
                ps.setDate(3, Date.valueOf(film.getReleaseDate()));
                ps.setInt(4, film.getDuration());
                return ps;
            }, keyHolder);
        } else {
            if (mpaDbStorage.findById(film.getMpa().getId()).isEmpty()) {
                throw new MpaNotFoundException(film.getMpa().getId());
            }
            jdbc.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO films (name, description, release_date, duration, mpa) VALUES (?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS
                );
                ps.setString(1, film.getName());
                ps.setString(2, film.getDescription());
                ps.setDate(3, Date.valueOf(film.getReleaseDate()));
                ps.setInt(4, film.getDuration());
                ps.setInt(5, film.getMpa().getId());
                return ps;
            }, keyHolder);
        }


        Integer generatedId = keyHolder.getKey().intValue();
        film.setId(generatedId);
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                if (genreDbStorage.getGenre(genre.getId()).isEmpty()) {
                    throw  new GenreNotFoundException(genre.getId());
                }
                genreDbStorage.addGenre(film.getId(), genre);
            }
        }
    }

    @Override
    public Optional<Film> findById(Integer id) {
        try {
            Film film = jdbc.queryForObject(
                    "SELECT * FROM films WHERE id = ?",
                    new FilmRowMapper(),
                    id
            );
            film.setGenres(new TreeSet<>(genreDbStorage.getGenres(id)));
            film.setMpa(mpaDbStorage.findById(film.getMpa().getId()).orElseThrow(() -> new MpaNotFoundException(id)));
            return Optional.ofNullable(film);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }



    @Override
    public void like(Integer userId, Integer filmId) {
        jdbc.update("INSERT INTO \"likes\" (film_id, user_id) VALUES (?, ?)",filmId,userId);
    }

    @Override
    public void unlike(Integer userId, Integer filmId) {
        jdbc.update("DELETE FROM \"likes\" WHERE film_id = ? AND user_id = ?",filmId,userId);
    }

    @Override
    public List<Film> getMostPopular(Integer count) {
        return jdbc.query("SELECT f.*\n" +
                "FROM films as f\n" +
                "LEFT JOIN \"likes\" AS l ON l.film_id = f.id\n" +
                "GROUP BY f.id\n" +
                "ORDER BY COUNT(*) DESC\n" +
                "LIMIT ?;",new FilmRowMapper(),count);
    }





    @Override
    public List<Film> getAll() {
        return jdbc.query("SELECT * FROM films",
                new FilmRowMapper());
    }

    @Override
    public void deleteById(Integer id) {
        jdbc.update("DELETE FROM films WHERE id = ?", id);
    }

    @Override
    public void update(Film film) {
        jdbc.update("UPDATE  films SET name = ?, description = ?, release_date = ?, duration = ?\n" +
                        "WHERE id = ?;",
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getId());
    }

    class FilmRowMapper implements RowMapper<Film> {

        @Override
        public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
            Film film = new Film();
            film.setId(rs.getInt("id"));
            film.setName(rs.getString("name"));
            film.setDescription(rs.getString("description"));
            film.setDuration(rs.getInt("duration"));
            film.setReleaseDate(rs.getDate("release_date").toLocalDate());
            film.setMpa(new Rating(rs.getInt("mpa"),""));

            return film;
        }
    }
}
