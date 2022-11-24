package net.lemixeur.repository;

import net.lemixeur.domain.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
public class UserRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;

    /**
     * Tests for basic methods (save, find, update, delete)
     */

    @Test
    public void shouldSaveUser() {
        User user = new User("email1@email.com", "password1");

        long expectedUserCount = repository.count() + 1;
        repository.save(user);

        assertThat(repository.count()).isEqualTo(expectedUserCount);
        assertNotNull(user.getId());
    }

    @Test
    public void shouldFindAllUsers() {
        User expectedUser1 = new User("email1@email.com", "password1");
        User expectedUser2 = new User("email2@email.com", "password2");
        User expectedUser3 = new User("email3@email.com", "password3");
        repository.save(expectedUser1);
        repository.save(expectedUser2);
        repository.save(expectedUser3);

        Iterable<User> actualUsers = repository.findAll();

        assertThat(actualUsers)
                .hasSize(3)
                .contains(expectedUser1, expectedUser2, expectedUser3);
    }

    @Test
    public void shouldFindUserById() {
        User expectedUser = new User("email1@email.com", "password1");
        repository.save(expectedUser);

        Optional<User> oActualUser = repository.findById(expectedUser.getId());

        assertAll(
                () -> assertTrue(oActualUser.isPresent()),
                () -> assertThat(oActualUser.get()).isEqualTo(expectedUser)
        );
    }

    @Test
    public void shouldDeleteUserById() {
        User user1 = new User("email1@email.com", "password1");
        User user2 = new User("email2@email.com", "password2");
        repository.save(user1);
        repository.save(user2);

        long expectedUserCount = repository.count() - 1;
        repository.deleteById(user2.getId());

        assertThat(repository.count()).isEqualTo(expectedUserCount);
    }

    /**
     * Tests for custom methods
     */

    @Test
    public void shouldFindUserByEmail() {
        User expectedUser = new User("email1@email.com", "password1");
        repository.save(expectedUser);

        Optional<User> oActualUser = repository.findByEmail(expectedUser.getEmail());

        assertAll(
                () -> assertTrue(oActualUser.isPresent()),
                () -> assertThat(oActualUser.get()).isEqualTo(expectedUser)
        );
    }
}