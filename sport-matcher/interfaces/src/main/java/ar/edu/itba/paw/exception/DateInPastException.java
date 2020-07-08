package ar.edu.itba.paw.exception;

@SuppressWarnings("serial")
public class DateInPastException extends InscriptionClosedException {

	public DateInPastException(String message) {
		super(message);
	}

}
