package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.*;
import java.util.List;
import java.util.Optional;

@Component("UserDbStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbc;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbc = jdbcTemplate;
    }

    @Override
    public void save(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO \"users\" (email, login, name, birthday) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setDate(4, Date.valueOf(user.getBirthday()));
            return ps;
        }, keyHolder);

        Integer generatedId = keyHolder.getKey().intValue();
        user.setId(generatedId);
    }


    @Override
    public Optional<User> findById(Integer id) {
        try {
            User user = jdbc.queryForObject(
                    "SELECT * FROM \"users\" WHERE id = ?",
                    new UserRowMapper(),
                    id
            );
            return Optional.of(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }




    @Override
    public List<User> getAll() {
        return jdbc.query("SELECT * FROM \"users\"",
                new UserRowMapper());
    }

    @Override
    public void deleteById(Integer id) {
        jdbc.update("DELETE FROM \"users\" WHERE id = ?", id);
    }

    @Override
    public void update(User film) {
        jdbc.update("UPDATE  \"users\" SET email = ?, login = ?, name = ?, birthday = ?\n" +
                        "WHERE id = ?;",
                film.getEmail(),
                film.getLogin(),
                film.getName(),
                film.getBirthday(),
                film.getId());
    }

    @Override
    public void addFriend(int userId, int friendId) {
        jdbc.update("INSERT INTO friendship (requester_id, addressee_id, status) VALUES \n" +
                        "(?, ?, ?);",
                userId,
                friendId,
                FriendshipStatus.PENDING.name());
    }

    @Override
    public void removeFriend(int userId, int friendId) {
        jdbc.update("DELETE FROM friendship WHERE  requester_id = ? AND addressee_id = ?",
                userId,
                friendId);
    }

    @Override
    public List<User> getSharedFriends(int userId, int secondUserId) {
        return jdbc.query("WITH user1_friends AS (" +
                        "    SELECT addressee_id AS friend_id " +
                        "    FROM friendship " +
                        "    WHERE requester_id = ? " +
                        "), " +
                        "user2_friends AS (" +
                        "    SELECT addressee_id AS friend_id " +
                        "    FROM friendship " +
                        "    WHERE requester_id = ? " +
                        ") " +
                        "SELECT u.* " +
                        "FROM \"users\" u " +
                        "WHERE u.id IN (" +
                        "    SELECT friend_id FROM user1_friends " +
                        "    INTERSECT " +
                        "    SELECT friend_id FROM user2_friends " +
                        ")",
                new UserRowMapper(),
                userId, secondUserId);
    }

    @Override
    public List<User> getFriendsById(int userId) {
        return jdbc.query("SELECT *\n" +
                "FROM \"users\" \n" +
                "WHERE id IN (\n" +
                "  SELECT addressee_id AS id\n" +
                "  FROM friendship\n" +
                "  WHERE requester_id = ?\n" +
                ");",
                new UserRowMapper(),
                userId);
    }

    class UserRowMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setLogin(rs.getString("login"));
            user.setBirthday(rs.getDate("birthday").toLocalDate());
            return user;
        }
    }
}
