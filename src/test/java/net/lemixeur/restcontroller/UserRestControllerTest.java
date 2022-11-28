package net.lemixeur.restcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.lemixeur.config.ProblemJsonConfiguration;
import net.lemixeur.domain.user.User;
import net.lemixeur.domain.user.UserInvalidProblem;
import net.lemixeur.domain.user.UserNotFoundProblem;
import net.lemixeur.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.zalando.problem.violations.Violation;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(ProblemJsonConfiguration.class)
@WebMvcTest(UserRestController.class)
public class UserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    public void createUser_Created() throws Exception {
        User expectedUser = new User();
        expectedUser.setEmail("email33@provider.com");
        expectedUser.setPassword("password33");

        Mockito.when(userService.save(Mockito.any(User.class))).thenReturn(expectedUser);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expectedUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.email", is(expectedUser.getEmail())));
    }

    @Test
    public void createUser_BadRequest() throws Exception {
        User user = new User();
        user.setEmail("bad-email");
        user.setPassword("pass");
        Violation expectedViolation1 = new Violation("email", "L'adresse courriel est invalide.");
        Violation expectedViolation2 = new Violation("password",
                "Le mot de passe doit contenir entre 8 et 32 caractères.");
        List<Violation> violations = List.of(expectedViolation1, expectedViolation2);

        Mockito.when(userService.save(Mockito.any(User.class))).thenThrow(new UserInvalidProblem(violations));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.violations[0].field", is(expectedViolation1.getField())))
                .andExpect(jsonPath("$.violations[0].message", is(expectedViolation1.getMessage())))
                .andExpect(jsonPath("$.violations[1].field", is(expectedViolation2.getField())))
                .andExpect(jsonPath("$.violations[1].message", is(expectedViolation2.getMessage())));
    }

    @Test
    public void getUserById_Ok() throws Exception {
        User expectedUser = new User();
        expectedUser.setId(777L);
        expectedUser.setEmail("email@provider.com");
        expectedUser.setPassword("password1");

        Mockito.when(userService.findById(777L)).thenReturn(expectedUser);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/user/777")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.email", is(expectedUser.getEmail())));
    }

    @Test
    public void getUserById_NotFound() throws Exception {
        Mockito.when(userService.findById(666L)).thenThrow(new UserNotFoundProblem(666L));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/user/666")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.title", is("Not found")))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.detail", is("Utilisateur '666' introuvable")));
    }

    @Test
    public void getAllUsers_Ok() throws Exception {
        User expectedUser1 = new User();
        expectedUser1.setId(1L);
        expectedUser1.setEmail("email1@provider.com");
        expectedUser1.setPassword("password1");
        User expectedUser2 = new User();
        expectedUser2.setId(2L);
        expectedUser2.setEmail("email2@provider.com");
        expectedUser2.setPassword("password2");

        Mockito.when(userService.findAll()).thenReturn(List.of(expectedUser1, expectedUser2));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].email", is(expectedUser2.getEmail())));
    }

    @Test
    public void getAllUsers_NotFound() throws Exception {
        Mockito.when(userService.findAll()).thenThrow(new UserNotFoundProblem());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.title", is("Not found")))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.detail", is("Aucun utilisateur trouvé")));
    }
}