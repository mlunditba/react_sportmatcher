package ar.edu.itba.paw.webapp.exception;

import ar.edu.itba.paw.exception.EntityNotFoundException;

@SuppressWarnings("serial")
public class EventNotFoundException extends EntityNotFoundException {

	public EventNotFoundException() {
		super("EventNotFound");
	}
	
	public EventNotFoundException(String msg) {
		super(msg);
	}

}
