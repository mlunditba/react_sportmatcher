package ar.edu.itba.paw.webapp.exception;

import ar.edu.itba.paw.exception.EntityNotFoundException;

@SuppressWarnings("serial")
public class ClubNotFoundException extends EntityNotFoundException {

	public ClubNotFoundException() {
		super("ClubNotFound");
	}

}
