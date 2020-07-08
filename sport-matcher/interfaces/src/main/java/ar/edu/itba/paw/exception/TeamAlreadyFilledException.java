package ar.edu.itba.paw.exception;

@SuppressWarnings("serial")
public class TeamAlreadyFilledException extends InscriptionClosedException {
	
	public TeamAlreadyFilledException() {
		super("TeamFull");
	}

}
