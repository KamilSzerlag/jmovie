package co.brick.kszerlag.jmovie.utils;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ObjectIdValidator.class)
@Documented
public @interface MongoId {

    String message() default "{Not proper ObjectId}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
