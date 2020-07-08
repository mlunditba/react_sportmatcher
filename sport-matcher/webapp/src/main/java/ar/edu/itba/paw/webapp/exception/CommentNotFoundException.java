package ar.edu.itba.paw.webapp.exception;

import ar.edu.itba.paw.exception.EntityNotFoundException;

@SuppressWarnings("serial")
public class CommentNotFoundException extends EntityNotFoundException {

	public CommentNotFoundException() {
		super("The requested comment was not found");
	}

}
