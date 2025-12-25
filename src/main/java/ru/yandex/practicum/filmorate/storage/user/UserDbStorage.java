package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Component
public class UserDbStorage implements UserStorage {

    private static final String SQL_INSERT_USER =
            "INSERT INTO \"users\" (email, login, name, birthday) VALUES (?, ?, ?, ?)";
    private static final String SQL_SELECT_USER_BY_ID =
            "SELECT * FROM \"users\" WHERE id = ?";
    private static final String SQL_SELECT_ALL_USERS =
            "SELECT * FROM \"users\"";
    private static final String SQL_DELETE_USER_BY_ID =
            "DELETE FROM \"users\" WHERE id = ?";
    private static final String SQL_UPDATE_USER =
            "UPDATE \"users\" SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
    private static final String SQL_INSERT_FRIENDSHIP =
            "INSERT INTO friendship (requester_id, addressee_id, status) VALUES (?, ?, ?)";
    private static final String SQL_DELETE_FRIENDSHIP =
            "DELETE FROM friendship WHERE requester_id = ? AND addressee_id = ?";
    private static final String SQL_SELECT_SHARED_FRIENDS =
            "WITH user1_friends AS (" +
                    "    SELECT addressee_id AS friend_id FROM friendship WHERE requester_id = ?" +
                    "), " +
                    "user2_friends AS (" +
                    "    SELECT addressee_id AS friend_id FROM friendship WHERE requester_id = ?" +
                    ") " +
                    "SELECT u.* FROM \"users\" u WHERE u.id IN (" +
                    "    SELECT friend_id FROM user1_friends " +
                    "    INTERSECT " +
                    "    SELECT friend_id FROM user2_friends" +
                    ")";
    private static final String SQL_SELECT_FRIENDS_BY_ID =
            "SELECT * FROM \"users\" WHERE id IN (" +
                    "  SELECT addressee_id AS id FROM friendship WHERE requester_id = ?" +
                    ")";
    private final JdbcTemplate jdbc;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbc = jdbcTemplate;
    }

    @Override
    public void save(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_INSERT_USER, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setDate(4, Date.valueOf(user.getBirthday()));
            return ps;
        }, keyHolder);
        user.setId(keyHolder.getKey().intValue());
    }

    @Override
    public Optional<User> findById(Integer id) {
        try {
            User user = jdbc.queryForObject(SQL_SELECT_USER_BY_ID, new UserRowMapper(), id);
            return Optional.of(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<User> getAll() {
        return jdbc.query(SQL_SELECT_ALL_USERS, new UserRowMapper());
    }

    @Override
    public void deleteById(Integer id) {
        jdbc.update(SQL_DELETE_USER_BY_ID, id);
    }

    @Override
    public void update(User film) {
        jdbc.update(SQL_UPDATE_USER,
                film.getEmail(),
                film.getLogin(),
                film.getName(),
                film.getBirthday(),
                film.getId());
    }

    @Override
    public void addFriend(int userId, int friendId) {
        jdbc.update(SQL_INSERT_FRIENDSHIP, userId, friendId, FriendshipStatus.PENDING.name());
    }

    @Override
    public void removeFriend(int userId, int friendId) {
        jdbc.update(SQL_DELETE_FRIENDSHIP, userId, friendId);
    }

    @Override
    public List<User> getSharedFriends(int userId, int secondUserId) {
        return jdbc.query(SQL_SELECT_SHARED_FRIENDS, new UserRowMapper(), userId, secondUserId);
    }

    @Override
    public List<User> getFriendsById(int userId) {
        return jdbc.query(SQL_SELECT_FRIENDS_BY_ID, new UserRowMapper(), userId);
    }


}
