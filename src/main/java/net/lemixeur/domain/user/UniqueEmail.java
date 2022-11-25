package net.lemixeur.domain.user;

import java.lang.annotation.*;
import javax.validation.*;

@Constraint(validatedBy = UniqueEmailValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface UniqueEmail {

    String message() default "Adresse courriel déjà utilisée.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}