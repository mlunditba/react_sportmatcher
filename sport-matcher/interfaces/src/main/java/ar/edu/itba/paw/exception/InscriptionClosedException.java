package ar.edu.itba.paw.exception;

@SuppressWarnings("serial")
public class InscriptionClosedException extends Exception {
	
	public InscriptionClosedException() {
		super("InscriptionClosed");
	}
	
	public InscriptionClosedException(String message) {
		super(message);
	}

}
