package ar.edu.itba.paw.exception;

@SuppressWarnings("serial")
public class UserAlreadyJoinedException extends UserBusyException {
	
	public UserAlreadyJoinedException() {
		super("AlreadyJoined");
	}

}
