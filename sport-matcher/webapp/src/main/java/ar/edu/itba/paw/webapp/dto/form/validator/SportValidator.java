package ar.edu.itba.paw.webapp.dto.form.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SportValidator implements ConstraintValidator<Sport, String> {

	@Override
	public void initialize(Sport constraintAnnotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		try {
			ar.edu.itba.paw.model.Sport.valueOf(value);
		} catch(IllegalArgumentException e) {
			return false;
		}
		return true;
	}

}
