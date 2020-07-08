package ar.edu.itba.paw.exception;

@SuppressWarnings("serial")
public class EventOverlapException extends EventCreationException {
	
	public EventOverlapException(String message) {
		super(message);
	}

}
