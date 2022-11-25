package net.lemixeur.util;

import org.zalando.problem.violations.Violation;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ViolationUtil {

    public static List<Violation> convertSetConstraintViolationToListViolation
            (Set<? extends ConstraintViolation<?>> constraintViolations) {
        List<Violation> violations = new ArrayList<>();

        if (!constraintViolations.isEmpty()) {
            constraintViolations.forEach(v -> {
                String message = v.getMessage();
                String field = v.getPropertyPath().toString();
                Violation violation = new Violation(field, message);
                violations.add(violation);
            });
        }

        return violations;
    }

}