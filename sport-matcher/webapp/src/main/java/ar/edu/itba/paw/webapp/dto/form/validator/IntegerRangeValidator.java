package ar.edu.itba.paw.webapp.dto.form.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IntegerRangeValidator implements ConstraintValidator<IntegerRange, Integer> {
	
	private int min;
	private int max;

	@Override
	public void initialize(IntegerRange constraintAnnotation) {
		min = constraintAnnotation.min();
		max = constraintAnnotation.max();
	}

	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {
		return value != null && value >= min && value <= max;
	}

}
