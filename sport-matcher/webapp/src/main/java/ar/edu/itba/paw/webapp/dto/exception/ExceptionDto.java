package ar.edu.itba.paw.webapp.dto.exception;

public class ExceptionDto {
	
	private String error;
	
	public static ExceptionDto ofException(Exception e) {
		final ExceptionDto dto = new ExceptionDto();
		dto.error = e.getMessage();
		
		return dto;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
