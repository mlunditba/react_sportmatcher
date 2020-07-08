package ar.edu.itba.paw.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.exception.IllegalParamException;
import ar.edu.itba.paw.exception.PictureProcessingException;
import ar.edu.itba.paw.interfaces.PitchDao;
import ar.edu.itba.paw.interfaces.PitchPictureService;
import ar.edu.itba.paw.interfaces.PitchService;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.Sport;

@Service
public class PitchServiceImpl implements PitchService {
	
	@Autowired
	private PitchDao pd;
	
	@Autowired
	private PitchPictureService pps;
	
	private static final String NEGATIVE_ID_ERROR = "Id must be greater than zero.";
	private static final String NEGATIVE_PAGE_ERROR = "Page must be greater than zero.";
	
	@Override
	public Optional<Pitch> findById(long pitchid) {
		if(pitchid <= 0) {
			throw new IllegalParamException(NEGATIVE_ID_ERROR);
		}
		return pd.findById(pitchid);
	}

	@Override
	public List<Pitch> findByClubId(long clubid, int page) {
		if(clubid <= 0 || page <= 0) {
			throw new IllegalParamException("Parameters must be greater than zero.");
		}
		return pd.findByClubId(clubid, page);
	}
	
	@Override
	public List<Pitch> findBy(Optional<String> name, Optional<Sport> sport,
			Optional<String> location, Optional<String> clubName, int page) {
		if(page <= 0) {
			throw new IllegalParamException(NEGATIVE_PAGE_ERROR);
		}
		String sportString = null;
		if(sport.isPresent()) {
			sportString = sport.get().toString();
		}
		return pd.findBy(name, Optional.ofNullable(sportString), location, clubName, page);
	}
	
	@Override
	public int countFilteredPitches(final Optional<String> pitchName, 
			final Optional<Sport> sport, final Optional<String> location, 
			final Optional<String> clubName) {
		String sportString = null;
		if(sport.isPresent()) {
			sportString = sport.get().toString();
		}
		return pd.countFilteredPitches(pitchName, Optional.ofNullable(sportString), location, clubName);
	}
	
	@Override
	public int countPitchPages(final int totalPitchQty) {
		return pd.countPitchPages(totalPitchQty);
	}

	@Transactional(rollbackFor = { Exception.class })
	@Override
	public Pitch create(Club club, String name, Sport sport, byte[] picture) 
			throws PictureProcessingException {
		Pitch pitch = pd.create(club, name, sport);
		if(picture != null) {
			pps.create(pitch, picture);
		}
		return pitch;
	}
	
	@Override
	public int getPageInitialPitchIndex(final int pageNum) {
		return pd.getPageInitialPitchIndex(pageNum);
	}
	
	@Transactional(rollbackFor = { Exception.class })
	@Override
	public void deletePitch(long pitchid) {
		pd.deletePitch(pitchid);
	}

	@Override
	public int countByClubId(long clubid) {
		return pd.countByClubId(clubid);
	}

}
