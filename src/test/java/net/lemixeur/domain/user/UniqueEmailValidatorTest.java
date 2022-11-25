package net.lemixeur.domain.user;

import net.lemixeur.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UniqueEmailValidatorTest {

    @Autowired
    private UserRepository repository;

    @Autowired
    private UniqueEmailValidator uniqueEmailValidator;

    @BeforeEach
    public void beforeEach() {
        User user = new User("email@provider.com", "password");
        repository.save(user);
    }

    @Test
    public void shouldBeValid() {
        boolean actualValidation = uniqueEmailValidator.isValid("email_new@provider.com", null);

        assertTrue(actualValidation);
    }

    @Test
    public void shouldBeInvalid() {
        boolean actualValidation = uniqueEmailValidator.isValid("email@provider.com", null);

        assertFalse(actualValidation);
    }

}
