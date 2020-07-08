package ar.edu.itba.paw.exception;

@SuppressWarnings("serial")
public class UserAlreadyExistsException extends Exception {
	
	public UserAlreadyExistsException() {
		super("UserAlreadyExists");
	}

}
