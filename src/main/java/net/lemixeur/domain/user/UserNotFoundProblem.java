package net.lemixeur.domain.user;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class UserNotFoundProblem extends AbstractThrowableProblem {

    public UserNotFoundProblem() {
        super(
            null,
            "Not found",
            Status.NOT_FOUND,
            "Aucun utilisateur trouvé");
    }

    public UserNotFoundProblem(Long userId) {
        super(
            null,
            "Not found",
            Status.NOT_FOUND,
            String.format("Utilisateur '%s' introuvable", userId));
    }

}