package ar.edu.itba.paw.webapp.dto.form.validator;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.edu.itba.paw.webapp.exception.FormValidationException;

@Component
public class FormValidator {
	
	@Autowired
	private Validator validator;

	public <T> void validate(T form, Class<?>... groups) throws FormValidationException {
		final Set<ConstraintViolation<T>> constraintViolations = validator.validate(form, groups);
		
		if (!constraintViolations.isEmpty())
			throw new FormValidationException("Invalid request", constraintViolations);
	}
}
