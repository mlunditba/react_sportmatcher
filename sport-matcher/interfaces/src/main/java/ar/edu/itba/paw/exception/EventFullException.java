package ar.edu.itba.paw.exception;

@SuppressWarnings("serial")
public class EventFullException extends InscriptionClosedException {

	public EventFullException() {
		super("EventFull");
	}

}
