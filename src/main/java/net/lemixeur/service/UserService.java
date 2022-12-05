package net.lemixeur.service;
import net.lemixeur.domain.user.User;
import net.lemixeur.domain.user.UserInvalidProblem;
import net.lemixeur.domain.user.UserNotFoundProblem;
import net.lemixeur.repository.UserRepository;
import net.lemixeur.util.ViolationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.zalando.problem.violations.Violation;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private Validator validator;

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Basic CRUD methods

    public User findById(Long id) {
        Optional<User> oUser = userRepository.findById(id);

        if (oUser.isEmpty()) {
            throw new UserNotFoundProblem(id);
        }

        return oUser.get();
    }

    public List<User> findAll() throws UserNotFoundProblem {
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            throw new UserNotFoundProblem();
        }

        return users;
    }

    public User save(User user) throws UserInvalidProblem {
        User newUser;

        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        List<Violation> violations = ViolationUtil.convertSetConstraintViolationToListViolation(constraintViolations);

        if (violations.isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            newUser = userRepository.save(user);
        } else {
            throw new UserInvalidProblem(violations);
        }

        return newUser;
    }

    // Other methods

    public boolean isEmailAlreadyInUse(String email) {
        boolean emailAlreadyInUse = false;

        Optional<User> oUser = userRepository.findByEmail(email);

        if (oUser.isPresent()) {
            emailAlreadyInUse = true;
        }

        return emailAlreadyInUse;
    }

}