package ar.edu.itba.paw.exception;

@SuppressWarnings("serial")
public class UserNotAuthorizedException extends Exception {

	public UserNotAuthorizedException(String message) {
		super(message);
	}
}
