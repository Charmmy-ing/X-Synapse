package com.charmmy.x_synapse.anno;

import com.charmmy.x_synapse.Validation.StateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(
        validatedBy = {StateValidator.class}
)
@Target({ ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)

public @interface State {
    String message() default "状态错误";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
