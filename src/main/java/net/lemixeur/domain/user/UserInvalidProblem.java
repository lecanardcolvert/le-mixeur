package net.lemixeur.domain.user;

import org.zalando.problem.Status;
import org.zalando.problem.violations.ConstraintViolationProblem;
import org.zalando.problem.violations.Violation;

import java.util.List;

public class UserInvalidProblem extends ConstraintViolationProblem {

    public UserInvalidProblem(List<Violation> violations) {
        super(
            Status.BAD_REQUEST,
            violations);
    }

}