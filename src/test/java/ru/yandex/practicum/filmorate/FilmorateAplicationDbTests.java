package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@Import(UserDbStorage.class)
public class FilmorateAplicationDbTests {
    private final UserDbStorage userStorage;

    @Autowired
    public FilmorateAplicationDbTests(UserDbStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Test
    public void testCreateUser() {
        var user = User.builder()
                .name("1")
                .login("1")
                .email("asg@mail.ru")
                .birthday(LocalDate.now())
                .build();

        userStorage.save(user);

        Assertions.assertTrue(true);
    }

    @Test
    public void testFindUserById() {
        var user = User.builder()
                .name("1")
                .login("1")
                .email("asg@mail.ru")
                .birthday(LocalDate.now())
                .build();

        userStorage.save(user);
        Optional<User> userOptional = userStorage.findById(user.getId());

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(u ->
                        assertThat(u).hasFieldOrPropertyWithValue("id", user.getId())
                );
    }
}
