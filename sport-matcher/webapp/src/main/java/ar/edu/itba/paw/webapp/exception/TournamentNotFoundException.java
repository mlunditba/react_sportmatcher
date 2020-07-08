package ar.edu.itba.paw.webapp.exception;

import ar.edu.itba.paw.exception.EntityNotFoundException;

@SuppressWarnings("serial")
public class TournamentNotFoundException extends EntityNotFoundException {

	public TournamentNotFoundException() {
		super("TournamentNotFound");
	}

}
