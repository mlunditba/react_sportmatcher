package ar.edu.itba.paw.interfaces;

import java.util.Optional;

import ar.edu.itba.paw.exception.EntityNotFoundException;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.Inscription;
import ar.edu.itba.paw.model.User;

public interface InscriptionDao {
	
	public Optional<Inscription> findByIds(final long eventid, final long userid);
	
	/**
	 * Gets the sum of User votes for an Event, or empty Optional if no votes.
	 * @param eventid	The Event's id.
	 * @return the sum of all User votes for that Event.
	 */
	public Optional<Integer> getVoteBalance(final long eventid);
	
	/**
	 * Gets the User's vote for that event.
	 * @param eventid	The Event's id.
	 * @param userid	The User's id.
	 * @return -1 if downvote, 1 if upvote or empty Optional if such vote does not exist.
	 */
	public Optional<Integer> getUserVote(final long eventid, final long userid);
	
	/**
	 * Sets a User's vote for an Event.
	 * @param isUpvote	True for upvote, false for downvote.
	 * @param eventid	The Event's id.
	 * @param userid	The User's id.
	 * @return 0 if the User doesn't have an inscription for that Event, 1 otherwise.
	 */
	public int vote(final boolean isUpvote, final long eventid, final long userid);
	
	public void deleteInscription(final long eventid, final long userid) throws EntityNotFoundException;

	/**
	 * Returns true if the commenter and receiver have an Inscription for a common past Event
	 * or if the commenter has an Inscription for a past Event which was owned by the receiver.
	 * @param commenter	The commenter.
	 * @param receiver	The receiver.
	 * @return true if the commenter and receiver have an Inscription for a common past Event
	 * or if the commenter has an Inscription for a past Event which was owned by the receiver.
	 */
	public boolean haveRelationship(final User commenter, final User receiver);
	
	/**
	 * Returns true if the User has an Inscription for a past Event which took place in the Club
	 * or if the User has owned a past Event which took place in the Club.
	 * @param commenter	The commenter.
	 * @param club		The Club.
	 * @return true if the User has an Inscription for a past Event which took place in the Club
	 * or if the User has owned a past Event which took place in the Club.
	 */
	public boolean haveRelationship(final User commenter, final Club club);
	
}
