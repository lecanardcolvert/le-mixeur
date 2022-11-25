package net.lemixeur.service;

import net.lemixeur.domain.user.User;
import net.lemixeur.domain.user.UserInvalidProblem;
import net.lemixeur.domain.user.UserNotFoundProblem;
import net.lemixeur.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    @Autowired
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void shouldFindUserById() {
        User expectedUser = new User(99L, "user@provider.com", "password");
        when(userRepository.findById(99L)).thenReturn(Optional.of(expectedUser));

        User actualUser = userService.findById(99L);

        assertThat(actualUser.getEmail()).isEqualTo("user@provider.com");
    }

    @Test
    public void shouldNotFindUserByIdThatDoesntExists() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundProblem.class, () -> userService.findById(getRandomLong()));
    }

    @Test
    public void shouldFindAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(new User(), new User()));

        assertThat(userService.findAll()).hasSize(2);
    }

    @Test
    public void shouldSaveValidUser() {
        User expectedUser = new User("email@provider.com", "password");
        when(userRepository.save(any(User.class))).thenReturn(expectedUser);

        User actualUser = userService.save(expectedUser);

        assertThat(actualUser).usingRecursiveComparison().isEqualTo(expectedUser);
    }

    @Test
    public void shouldNotSaveInvalidUser() {
        User user = new User("bad-email", "password");

        Assertions.assertThrows(UserInvalidProblem.class, () -> userService.save(user));
    }

    @Test
    public void emailShouldBeAlreadyInUse() {
        User user = new User("user@provider.com", "password");
        when(userRepository.findByEmail("email@provider.com")).thenReturn(Optional.of(user));

        boolean actualEmailAlreadyInUse = userService.isEmailAlreadyInUse("email@provider.com");

        assertThat(actualEmailAlreadyInUse).isEqualTo(true);
    }


    @Test
    public void emailShouldNotBeAlreadyInUse() {
        when(userRepository.findByEmail("email@provider.com")).thenReturn(Optional.empty());

        boolean actualEmailAlreadyInUse = userService.isEmailAlreadyInUse("email@provider.com");

        assertThat(actualEmailAlreadyInUse).isEqualTo(false);
    }

    private Long getRandomLong() {
        return new Random().longs(1, 10).findFirst().getAsLong();
    }

}