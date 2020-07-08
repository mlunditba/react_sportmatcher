package ar.edu.itba.paw.interfaces;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import ar.edu.itba.paw.exception.DateInPastException;
import ar.edu.itba.paw.exception.EndsBeforeStartsException;
import ar.edu.itba.paw.exception.EntityNotFoundException;
import ar.edu.itba.paw.exception.EventFullException;
import ar.edu.itba.paw.exception.EventNotFinishedException;
import ar.edu.itba.paw.exception.EventOverlapException;
import ar.edu.itba.paw.exception.HourOutOfRangeException;
import ar.edu.itba.paw.exception.InscriptionClosedException;
import ar.edu.itba.paw.exception.MaximumDateExceededException;
import ar.edu.itba.paw.exception.UserAlreadyJoinedException;
import ar.edu.itba.paw.exception.UserBusyException;
import ar.edu.itba.paw.exception.UserNotAuthorizedException;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.model.User;

public interface EventService {

	public Optional<Event> findByEventId(final long eventid);

	/**
	 * Gets Events owned by a User.
	 * @param futureEvents 	Finds only future Events (true) or only past Events (false).
	 * @param userid 		Owner of Events.
	 * @param pageNum 		Page number.
	 * @return The list of owned Events.
	 */
	public List<Event> findByOwner(final boolean futureEvents, final long userid, final int pageNum);
	
	/**
	 * Counts Events owned by a User.
	 * @param futureEvents 	Finds only future Events (true) or only past Events (false).
	 * @param userid 		Owner of Events.
	 * @return The amount of owned Events.
	 */
	public int countByOwner(final boolean futureEvents, final long userid);
	
	/**
	 * Counts the amount of Events for which a User has an inscription.
	 * @param futureEvents	Finds only future Events (true) or only past Events (false).
	 * @param userid		The User's id.
	 * @return the amount of events for which a User has an inscription.
	 */
	public Integer countByUserInscriptions(final boolean futureEvents, final long userid);
	
	/**
	 * Gets past Events for which a User has an inscription.
	 * @param userid		Inscripted User's id.
	 * @param pageNum		Page number.
	 * @return the list of past events for which a User has an inscription.
	 */
	public List<Event> findPastUserInscriptions(long userid, int pageNum);
	
	/**
	 * Gets future Events for which a User has an inscription. Max results = 24 * 7.
	 * @param userid		Inscripted User's id.
	 * @param withinWeek	Get events within current week (true) or without restriction (false).
	 * @return the list of future events for which a User has an inscription.
	 */
	public List<Event> findFutureUserInscriptions(final long userid, final boolean withinWeek);

	/**
	 * Finds Events in a Pitch. Only seven days of Events will be returned.
	 * @param pitchid 	The id of the Pitch.
	 * @return a list of Events (with a maximum size of 24 * 7).
	 */
	public List<Event> findCurrentEventsInPitch(final long pitchid);

	/**
	 * Returns a list of Events matching present filters and sets the inscriptions field.
	 * @param onlyFuture		Search only future Events (true) or any Events (false).
	 * @param eventName			String to match an Event's name with.
	 * @param clubName			String to match an Event's club name with.
	 * @param sport				String to match an Event's Sport with.
	 * @param organizer			String to match an Event's organizer's name with.
	 * @param vacancies			Minimum vacancies for an Event.
	 * @param pageNum			Page number.
	 * @return  The list of Events matching present filters.
	 */
	public List<Event> findByWithInscriptions(boolean onlyFuture, Optional<String> eventName, 
			Optional<String> clubName, Optional<Sport> sport, Optional<String> organizer,
			Optional<Integer> vacancies, Optional<Instant> date, int pageNum);

	/**
	 * Returns a list of Events matching present filters.
	 * @param onlyFuture		Search only future Events (true) or any Events (false).
	 * @param eventName			String to match an Event's name with.
	 * @param clubName			String to match an Event's club name with.
	 * @param sport				String to match an Event's Sport with.
	 * @param organizer			String to match an Event's organizer's name with.
	 * @param vacancies			Minimum vacancies for an Event.
	 * @param pageNum			Page number.
	 * @return The list of Events matching present filters.
	 */
	public List<Event> findBy(boolean onlyFuture, Optional<String> eventName, Optional<String> clubName,
			Optional<Sport> sport, Optional<String> organizer, Optional<Integer> vacancies,
			Optional<Instant> date, int pageNum);
	
	public Integer countFilteredEvents(final boolean onlyFuture, final Optional<String> eventName, 
			final Optional<String> clubName, final Optional<Sport> sport, Optional<String> organizer, 
			final Optional<Integer> vacancies, final Optional<Instant> date);

	public List<List<Event>> convertEventListToSchedule(List<Event> events);

	public int countFutureEventPages();

	/**
	 * Gets the amount of Users currently participating in an Event.
	 * @param	eventid		The Event's id
	 * @return the amount of Users currently participating in the Event.
	 */
	public int countParticipants(long eventid);

	public Event create(final String name, final User owner, final Pitch pitch, final String description,
			final int maxParticipants, final Instant date, final int startsAtHour,
			final int endsAtHour, final Instant inscriptionEndDate) throws MaximumDateExceededException, EndsBeforeStartsException,
											EventOverlapException, HourOutOfRangeException, DateInPastException;

	public void joinEvent(final long userid, final long eventid)
			throws UserAlreadyJoinedException, EventFullException, UserBusyException, InscriptionClosedException, EntityNotFoundException;

	public void leaveEvent(final long eventid, final long userid) throws DateInPastException, EntityNotFoundException;

	public void kickFromEvent(final User owner, final long kickedUserId, final Event event)
			throws UserNotAuthorizedException, DateInPastException, EntityNotFoundException;

	/**
	 * Returns a User's favorite Sport(s) based on Events joined.
	 * @param 	userid 	The User's id.
	 * @return the User's favorite Sports.
	 */
	public Optional<Sport> getFavoriteSport(final long userid);

	/**
	 * Gets a User's favorite Club(s) based on Events joined.
	 * @param 	userid 	The User's id.
	 * @return the User's favorite Clubs.
	 */
	public Optional<Club> getFavoriteClub(final long userid);
	
	/**
	 * Gets the page's first Event's index in the overall filtered Events.
	 * @param pageNum	The page's number.
	 * @return the page's first Event's index.
	 */
	public int getPageInitialEventIndex(final int pageNum);

	/**
	 * Deletes an Event from database along with all User related participations.
	 * @param	eventid		The Event's id.
	 * @throws DateInPastException If the event already took place
	 * @throws EntityNotFoundException If the event is not found
	 */
	public void deleteEvent(long eventid) throws DateInPastException, EntityNotFoundException;
	
	/**
	 * Gets the sum of User votes for an Event.
	 * @param eventid	The Event's id.
	 * @return the sum of all User votes for that Event.
	 */
	public int getVoteBalance(final long eventid);
	
	/**
	 * Gets the User's vote for that event.
	 * @param eventid	The Event's id.
	 * @param userid	The User's id.
	 * @return -1 if downvote, 1 if upvote or 0 if such vote does not exist.
	 */
	public int getUserVote(final long eventid, final long userid);
	
	/**
	 * Sets a User's vote for an Event.
	 * @param isUpvote	True for upvote, false for downvote.
	 * @param eventid	The Event's id.
	 * @param userid	The User's id.
	 */
	public void vote(final boolean isUpvote, final Event event, final long userid)
			throws UserNotAuthorizedException, EventNotFinishedException;
	
	/**
	 * Gets the amount of pages the list of inscripted Events occupies.
	 * @param onlyFuture	Search only for future Events (true) or only for past Events (false)
	 * @param userid		The User's id.
	 * @return The amount of pages.
	 */
	public int countUserInscriptionPages(final boolean onlyFuture, final long userid);
	
	/**
	 * Gets the amount of pages the list of owned Events occupies.
	 * @param onlyFuture	Search only for future Events (true) or only for past Events (false)
	 * @param userid		The User's id.
	 * @return The amount of pages.
	 */
	public int countUserOwnedPages(final boolean onlyFuture, final long userid);

	public void cancelEvent(final Event event, final long userid) 
			throws UserNotAuthorizedException, DateInPastException;

	public void checkUncompletedEvents();

	public int countEventPages(final int totalEventQty);

	public int getMinHour();

	public int getMaxHour();

}
