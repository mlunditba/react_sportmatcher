package ar.edu.itba.paw.interfaces;

import java.util.List;
import java.util.Optional;

import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.Sport;

public interface PitchDao {
	
	public Optional<Pitch> findById(long pitchid);
	
	public List<Pitch> findByClubId(long clubid, int page);
	
	public int countByClubId(long clubid);
	
	public List<Pitch> findBy(Optional<String> name, Optional<String> sport,
			Optional<String> location, Optional<String> clubName, int page);
	
	public int countFilteredPitches(final Optional<String> pitchName, 
			final Optional<String> sport, final Optional<String> location, 
			final Optional<String> clubName);
	
	public int countPitchPages(final int totalPitchQty);
	
	public Pitch create(Club club, String name, Sport sport);
	
	/**
	 * Gets the page's first Pitch's index in the overall filtered Pitches.
	 * @param pageNum	The page's number.
	 * @return the page's first Pitch's index.
	 */
	public int getPageInitialPitchIndex(final int pageNum);
	
	public void deletePitch(long pitchid);

}
