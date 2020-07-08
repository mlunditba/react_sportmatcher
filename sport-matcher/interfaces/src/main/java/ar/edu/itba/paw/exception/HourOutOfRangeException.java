package ar.edu.itba.paw.exception;

@SuppressWarnings("serial")
public class HourOutOfRangeException extends EventCreationException {
	
	public HourOutOfRangeException(int from, int to) {
		super("Events can only take place between " + from + " hs and " + to + " hs");
	}
}
