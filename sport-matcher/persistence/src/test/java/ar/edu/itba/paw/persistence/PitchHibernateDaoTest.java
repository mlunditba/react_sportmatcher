package ar.edu.itba.paw.persistence;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.interfaces.PitchDao;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.Sport;

@Sql("classpath:schema.sql")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class PitchHibernateDaoTest {
	
	@Autowired
	private PitchDao pd;
	
	private static final long PITCHID = 1;
	private static final long CLUBID = 1;
	private static final Club CLUB = new Club(CLUBID, "name", "location", Instant.now());
	private static final String NAME = "pitch";
	private static final String LOCATION = "location";
	private static final String CLUBNAME = "club";
	
	@Rollback
	@Test
	public void testCreate() {
		final Pitch pitch = pd.create(CLUB, NAME, Sport.TENNIS);
		Assert.assertNotNull(pitch);
		Assert.assertEquals(CLUBID, pitch.getClub().getClubid());
		Assert.assertEquals(NAME, pitch.getName());
		Assert.assertEquals(Sport.TENNIS, pitch.getSport());
		Assert.assertNotNull(pitch.getCreatedAt());
	}
	
	@Test
	public void testFindById() {
		final Optional<Pitch> pitch = pd.findById(PITCHID);
		Assert.assertTrue(pitch.isPresent());
		Assert.assertEquals(PITCHID, pitch.get().getPitchid());
		Assert.assertEquals(CLUBID, pitch.get().getClub().getClubid());
		Assert.assertEquals(NAME, pitch.get().getName());
		Assert.assertEquals(Sport.SOCCER, pitch.get().getSport());
		Assert.assertNotNull(pitch.get().getCreatedAt());
	}
	
	@Test
	public void testFindByClubId() {
		final List<Pitch> pitches = pd.findByClubId(CLUBID, 1);
		Assert.assertEquals(2, pitches.size());
		Assert.assertEquals(PITCHID, pitches.get(0).getPitchid());
		Assert.assertEquals(CLUBID, pitches.get(0).getClub().getClubid());
		Assert.assertEquals(NAME, pitches.get(0).getName());
		Assert.assertEquals(Sport.SOCCER, pitches.get(0).getSport());
		Assert.assertNotNull(pitches.get(0).getCreatedAt());
		
	}
	
	@Test
	public void testFindBy() {
		final List<Pitch> pitches = pd.findBy(
				Optional.of(NAME), 
				Optional.of(Sport.SOCCER.toString()),
				Optional.of(LOCATION),
				Optional.of(CLUBNAME),
				1);
		Assert.assertEquals(2, pitches.size());
		Assert.assertEquals(PITCHID, pitches.get(0).getPitchid());
		Assert.assertEquals(CLUBID, pitches.get(0).getClub().getClubid());
		Assert.assertEquals(NAME, pitches.get(0).getName());
		Assert.assertEquals(Sport.SOCCER, pitches.get(0).getSport());
		Assert.assertNotNull(pitches.get(0).getCreatedAt());
	}
		
	@Test
	public void testFindByNoMatches() {
		final List<Pitch> noPitches = pd.findBy(
				Optional.of(NAME), 
				Optional.of(Sport.TENNIS.toString()),
				Optional.of(LOCATION),
				Optional.of(CLUBNAME),
				1);
		Assert.assertEquals(0, noPitches.size());
	}
	
	@Test
	public void testFindByTwoMatch() {
		final List<Pitch> pitches2 = pd.findBy(
				Optional.empty(),
				Optional.of(Sport.SOCCER.toString()),
				Optional.empty(),
				Optional.of(CLUBNAME),
				1);
		Assert.assertEquals(2, pitches2.size());
	}
	
	@Test
	public void testFindTwo() {
		final List<Pitch> invalidPage = pd.findBy(
				Optional.of(NAME), 
				Optional.of(Sport.SOCCER.toString()),
				Optional.of(LOCATION),
				Optional.of(CLUBNAME),
				2);
		Assert.assertEquals(0, invalidPage.size());
	}
	
	@Test
	public void testCountFiltered() {
		int count = pd.countFilteredPitches(
				Optional.of(NAME), 
				Optional.of(Sport.SOCCER.toString()),
				Optional.of(LOCATION),
				Optional.of(CLUBNAME));
		Assert.assertEquals(2, count);
	}
	
	@Test
	public void countFilteredTwo() {
		int count = pd.countFilteredPitches(
				Optional.of(NAME), 
				Optional.empty(),
				Optional.empty(),
				Optional.of(CLUBNAME));
		Assert.assertEquals(2, count);
	}
	
	@Test
	public void countFilteredNone() {
		int count = pd.countFilteredPitches(
				Optional.of("BADSTRING"), 
				Optional.of(Sport.SOCCER.toString()),
				Optional.of(LOCATION),
				Optional.of(CLUBNAME));
		Assert.assertEquals(0, count);
	}

}
