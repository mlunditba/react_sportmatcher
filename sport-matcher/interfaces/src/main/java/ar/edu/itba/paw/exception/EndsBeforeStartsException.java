package ar.edu.itba.paw.exception;

@SuppressWarnings("serial")
public class EndsBeforeStartsException extends EventCreationException {
	
	public EndsBeforeStartsException() {
		super("EndsBeforeStarts");
	}

}
