package ar.edu.itba.paw.webapp.exception;

import ar.edu.itba.paw.exception.EntityNotFoundException;

@SuppressWarnings("serial")
public class PitchNotFoundException extends EntityNotFoundException {

	public PitchNotFoundException() {
		super("PitchNotFound");
	}

}
