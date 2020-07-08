package ar.edu.itba.paw.webapp.exception;

@SuppressWarnings("serial")
public class TournamentEventNotFoundException extends EventNotFoundException {
	
	public TournamentEventNotFoundException() {
		super("TournamentEventNotFound");
	}

}
