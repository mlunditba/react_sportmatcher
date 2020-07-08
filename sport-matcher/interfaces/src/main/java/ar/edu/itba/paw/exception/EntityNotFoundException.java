package ar.edu.itba.paw.exception;

@SuppressWarnings("serial")
public class EntityNotFoundException extends Exception {
	
	public EntityNotFoundException(String msg) {
		super(msg);
	}
	
	public EntityNotFoundException() {
		super("The entity was not found.");
	}

}
