package ar.edu.itba.paw.interfaces;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.ClubComment;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.model.User;

public interface ClubDao {
	
	public Optional<Club> findById(long clubid);
	
	public List<Club> findAll(int page);
	
	public List<Club> findBy(Optional<String> clubName, Optional<String> location, int page);
	
	public int countFilteredClubs(Optional<String> clubName, Optional<String> location);
	
	public Club create(String name, String location);
	
	/**
	 * Gets the page's first Club's index in the overall filtered Clubs.
	 * @param pageNum	The page's number.
	 * @return the page's first Club's index.
	 */
	public int getPageInitialClubIndex(final int pageNum);
	
	public void deleteClub(long clubid);

	public int countClubPages(final int totalClubQty);
	
	/**
	 * Gets the amount of Events which took place in a Club.
	 * @param clubid	The id of the Club.
	 * @return the amount of past Events for that Club.
	 */
	public int countPastEvents(final long clubid);
	
	public ClubComment createComment(final User commenter, final Club club, final String comment);
	
	public Optional<ClubComment> findComment(final long commentid);
	
	public List<ClubComment> getCommentsByClub(final long clubid, final int pageNum);
	
	public int countByClubComments(final long clubid);
	
	public int getCommentsPageInitIndex(final int pageNum);

	public int getCommentsMaxPage(final long clubid);
	
	public List<Pitch> getAvailablePitches(final long clubid, final Sport sport, 
			Instant startsAt, Instant endsAt, int amount);

	public List<Event> findCurrentEventsInClub(final long clubid, final Sport sport);

}
