package net.lemixeur.restcontroller;

import net.lemixeur.domain.user.User;
import net.lemixeur.domain.user.UserInvalidProblem;
import net.lemixeur.domain.user.UserNotFoundProblem;
import net.lemixeur.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserRestController {

    private UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/user")
    public User createUser(@RequestBody User user) throws UserInvalidProblem {
        return userService.save(user);
    }

    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable Long id) throws UserNotFoundProblem {
        return userService.findById(id);
    }

    @GetMapping("/users")
    public List<User> getAllUsers() throws UserNotFoundProblem {
        return userService.findAll();
    }

}