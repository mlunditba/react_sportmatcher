package ar.edu.itba.paw.webapp.dto.exception;

import java.util.Set;
import java.util.stream.Collectors;

import ar.edu.itba.paw.webapp.exception.FormValidationException;

public class FormValidationExceptionDto {
	
	private String message;
	private Set<ConstraintViolationDto> constraintViolations;
	
	public static FormValidationExceptionDto ofException(FormValidationException e) {
		FormValidationExceptionDto dto = new FormValidationExceptionDto();
		dto.message = e.getMessage();
		dto.constraintViolations = e.getConstraintViolations().stream().map(ConstraintViolationDto::ofConstraintViolation)
				.collect(Collectors.toSet());
		
		return dto;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Set<ConstraintViolationDto> getConstraintViolations() {
		return constraintViolations;
	}

	public void setConstraintViolations(Set<ConstraintViolationDto> constraintViolations) {
		this.constraintViolations = constraintViolations;
	}

}
