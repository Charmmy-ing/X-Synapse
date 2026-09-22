package com.charmmy.x_synapse.Validation;

import com.charmmy.x_synapse.anno.State;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StateValidator implements ConstraintValidator<State, Integer> {
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == 0 || value == 1) {
            return true;
        }
        return false;
    }
}
