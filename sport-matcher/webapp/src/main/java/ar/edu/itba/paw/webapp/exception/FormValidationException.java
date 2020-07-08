package ar.edu.itba.paw.webapp.exception;

import java.util.Set;

import javax.validation.ConstraintViolation;

@SuppressWarnings("serial")
public class FormValidationException extends Exception {
	
	private final Set<? extends ConstraintViolation<?>> constraintViolations;

	public FormValidationException(String msg, Set<? extends ConstraintViolation<?>> constraintViolations) {
		super(msg);
		this.constraintViolations = constraintViolations;
	}

	public Set<? extends ConstraintViolation<?>> getConstraintViolations() {
		return constraintViolations;
	}
}
