package ar.edu.itba.paw.exception;

@SuppressWarnings("serial")
public class UserBusyException extends Exception {

	public UserBusyException() {
		super("UserBusy");
	}
	
	public UserBusyException(String message) {
		super(message);
	}
}
