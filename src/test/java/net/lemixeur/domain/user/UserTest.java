package net.lemixeur.domain.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserTest {

    @Autowired
    private Validator validator;

    @Test
    public void shouldNotValidateEmailNull() {
        User userNullEmail = new User(null, "password");

        Set<ConstraintViolation<User>> scvNullEmail = validator.validate(userNullEmail);
        ConstraintViolation<User> violationNullEmail = scvNullEmail.iterator().next();

        assertEquals(1, scvNullEmail.size());
        assertEquals("email", violationNullEmail.getPropertyPath().toString());
        assertEquals("L'adresse courriel ne peut être vide.", violationNullEmail.getMessage());
    }

    @Test
    public void shouldNotValidateEmailBlank() {
        User userEmptyEmail = new User("", "password");

        Set<ConstraintViolation<User>> scvEmptyEmail = validator.validate(userEmptyEmail);
        ConstraintViolation<User> violationEmptyEmail = scvEmptyEmail.iterator().next();

        assertEquals(1, scvEmptyEmail.size());
        assertEquals("email", violationEmptyEmail.getPropertyPath().toString());
        assertEquals("L'adresse courriel est invalide.", violationEmptyEmail.getMessage());
    }

    @Test
    public void shouldNotValidateEmailInvalidFormat() {
        User userInvalidFormatEmail = new User("email@", "password");

        Set<ConstraintViolation<User>> scvInvalidFormatEmail =
                validator.validate(userInvalidFormatEmail);
        ConstraintViolation<User> violationInvalidFormatEmail = scvInvalidFormatEmail.iterator().next();

        assertEquals(1, scvInvalidFormatEmail.size());
        assertEquals("email", violationInvalidFormatEmail.getPropertyPath().toString());
        assertEquals("L'adresse courriel est invalide.", violationInvalidFormatEmail.getMessage());
    }

    @Test
    public void shouldValidateEmailValidFormat() {
        User userValidEmail = new User("user@provider.com", "password");

        Set<ConstraintViolation<User>> scvValidEmail = validator.validate(userValidEmail);

        assertEquals(0, scvValidEmail.size());
    }

    @Test
    public void shouldNotValidatePasswordNull() {
        User user = new User("email@provider.com", null);

        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        ConstraintViolation<User> violation = constraintViolations.iterator().next();

        assertEquals(1, constraintViolations.size());
        assertEquals("password", violation.getPropertyPath().toString());
        assertEquals("Le mot de passe ne peut être vide.", violation.getMessage());
    }

    @Test
    public void shouldNotValidatePasswordTooShort() {
        User user = new User("email@provider.com", "1234567");

        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        ConstraintViolation<User> violation = constraintViolations.iterator().next();

        assertEquals(1, constraintViolations.size());
        assertEquals("password", violation.getPropertyPath().toString());
        assertEquals("Le mot de passe doit contenir entre 8 et 32 caractères.", violation.getMessage());
    }

    @Test
    public void shouldNotValidatePasswordTooLong() {
        // Length 33
        User user = new User("email@provider.com", "0123456789012345678901234567890123");

        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        ConstraintViolation<User> violation = constraintViolations.iterator().next();

        assertEquals(1, constraintViolations.size());
        assertEquals("password", violation.getPropertyPath().toString());
        assertEquals("Le mot de passe doit contenir entre 8 et 32 caractères.", violation.getMessage());
    }

    @Test
    public void shouldValidatePasswordGoodLength() {
        User user = new User("email@provider.com", "abcdefghijkl");

        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);

        assertEquals(0, constraintViolations.size());
    }

}