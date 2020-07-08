package ar.edu.itba.paw.exception;

@SuppressWarnings("serial")
public class MaximumDateExceededException extends EventCreationException {

	public MaximumDateExceededException() {
		super("New events cannot start more than seven days after today");
	}
	
	public MaximumDateExceededException(String message) {
		super(message);
	}
}
