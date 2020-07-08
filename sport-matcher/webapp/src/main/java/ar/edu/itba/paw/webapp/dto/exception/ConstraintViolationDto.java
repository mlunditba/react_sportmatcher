package ar.edu.itba.paw.webapp.dto.exception;

import javax.validation.ConstraintViolation;

public class ConstraintViolationDto {
	
	private String propertyPath;
	private String message;
	
	public static <T> ConstraintViolationDto ofConstraintViolation(ConstraintViolation<T> cv) {
		ConstraintViolationDto dto = new ConstraintViolationDto();
		dto.propertyPath = cv.getPropertyPath().toString();
		dto.message = cv.getMessage();
		
		return dto;
	}

	public String getPropertyPath() {
		return propertyPath;
	}

	public void setPropertyPath(String propertyPath) {
		this.propertyPath = propertyPath;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
