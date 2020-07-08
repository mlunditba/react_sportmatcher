package ar.edu.itba.paw.persistence;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.exception.UserBusyException;
import ar.edu.itba.paw.interfaces.EventDao;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Inscription;
import ar.edu.itba.paw.model.InscriptionId;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.model.User;

@Sql("classpath:schema.sql")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class EventHibernateDaoTest {

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private EventDao ed;

	private static final long EVENTID = 2;
	private static final long USERID = 1;
	private static final String EVENTNAME = "event";
	private static final long CLUBID = 1;
	private static final Club CLUB = new Club(1, "club", "location", Instant.now());
	private static final long PITCHID = 1;
	private static final Pitch PITCH = new Pitch(1, CLUB, "pitch", Sport.TENNIS, Instant.now());
	private static final String DESCRIPTION = "description";
	private static final int MAX_PARTICIPANTS = 2;
	private static final Instant STARTS_AT = Instant.now().plus(2, ChronoUnit.DAYS);
	private static final Instant INSCRIPTION_ENDS_AT = STARTS_AT.minus(2, ChronoUnit.DAYS);
	private static final int DURATION = 1;
	private static final Instant ENDS_AT = STARTS_AT.plus(DURATION, ChronoUnit.HOURS);
	private static final Timestamp STARTS = Timestamp.valueOf("2030-05-20 10:00:00");
	private static final Timestamp ENDS = Timestamp.valueOf("2030-05-20 11:00:00");
	private static final Timestamp INSCRIPTION_ENDS = Timestamp.valueOf("2030-05-18 11:00:00");
	private static final InscriptionId INSCRIPTION_ID = new InscriptionId(EVENTID, USERID);
	
	@Rollback
	@Test
	public void testCreateEvent() throws Exception {
		final Event event = ed.create(EVENTNAME, em.find(User.class, USERID), em.find(Pitch.class, PITCHID), DESCRIPTION, 
				MAX_PARTICIPANTS, STARTS_AT, ENDS_AT, INSCRIPTION_ENDS_AT);
		Assert.assertNotNull(event);
		Assert.assertEquals(EVENTNAME, event.getName());
		Assert.assertEquals(PITCH, event.getPitch());
		Assert.assertEquals(DESCRIPTION, event.getDescription());
		Assert.assertEquals(MAX_PARTICIPANTS, event.getMaxParticipants());
		Assert.assertEquals(STARTS_AT, event.getStartsAt());
		Assert.assertEquals(ENDS_AT, event.getEndsAt());
		Assert.assertEquals(INSCRIPTION_ENDS_AT, event.getEndsInscriptionAt());
		Assert.assertNotNull(event.getCreatedAt());
	}

	@Test
	public void testFindById() {
		final Optional<Event> event = ed.findByEventId(EVENTID);
		Assert.assertTrue(event.isPresent());
		Assert.assertEquals(EVENTNAME, event.get().getName());
		Assert.assertEquals(PITCH.getPitchid(), event.get().getPitch().getPitchid());
		Assert.assertEquals(DESCRIPTION, event.get().getDescription());
		Assert.assertEquals(MAX_PARTICIPANTS, event.get().getMaxParticipants());
		Assert.assertEquals(STARTS.toInstant(), event.get().getStartsAt());
		Assert.assertEquals(ENDS.toInstant(), event.get().getEndsAt());
		Assert.assertEquals(INSCRIPTION_ENDS.toInstant(), event.get().getEndsInscriptionAt());
		Assert.assertNotNull(event.get().getCreatedAt());
	}
	
	@Test
	public void testFindByOwnerFuture() {
		List<Event> events = ed.findByOwner(true, USERID, 1);
		Assert.assertEquals(1, events.size());
	}
	
	@Test
	public void testFindByOwnerPast() {
		List<Event> pastEvents = ed.findByOwner(false, USERID, 1);
		Assert.assertEquals(1, pastEvents.size());
	}
	
	@Test
	public void testFindCurrentEventsInPitch() {
		List<Event> events = ed.findCurrentEventsInPitch(PITCH.getPitchid());
		Assert.assertEquals(0, events.size()); // Inserted events are either in the past or years from now
	}
	
	@Test
	public void testCountFilteredEvents() {
		int count = ed.countFilteredEvents(
				false,
				Optional.of(EVENTNAME),
				Optional.of(CLUB.getName()),
				Optional.empty(),
				Optional.empty(),
				Optional.of(1),
				Optional.empty());
		Assert.assertEquals(1, count);
	}
	
	@Test
	public void testCountFilteredEventsVarious() {
		int count = ed.countFilteredEvents(
				true,
				Optional.of(EVENTNAME),
				Optional.of(CLUB.getName()),
				Optional.empty(),
				Optional.empty(),
				Optional.of(1),
				Optional.empty());
		Assert.assertEquals(0, count);
	}
	
	@Test
	public void testCountFilteredEventsCurrentDate() {
		int count = ed.countFilteredEvents(
				true,
				Optional.empty(),
				Optional.empty(),
				Optional.empty(),
				Optional.empty(),
				Optional.empty(),
				Optional.of(Instant.now()));
		Assert.assertEquals(0, count);
	}
	
	@Test
	public void testCountFutureEventPages() {
		Assert.assertEquals(1, ed.countFutureEventPages());
	}
	
	@Test
	public void testCountParticipants() {
		Assert.assertEquals(2, ed.countParticipants(EVENTID));
	}
	
	@Rollback
	@Test
	public void testJoinEvent() throws Exception {
		try {
			em.remove(em.find(Inscription.class, INSCRIPTION_ID));
		} catch(Exception e) {
			// The inscription did not exist and was not deleted
		}
		
		ed.joinEvent(em.find(User.class, USERID), em.find(Event.class, EVENTID));
		Assert.assertNotNull(em.find(Inscription.class, INSCRIPTION_ID));
	}
	
	@Rollback
	@Test
	public void testUserBusyWhenJoiningEvent() throws Exception {
		try {
			ed.joinEvent(em.find(User.class, USERID), em.find(Event.class, EVENTID));
			Assert.assertTrue(false);
		} catch(Exception e) {
			Assert.assertEquals(UserBusyException.class, e.getClass());
		}
	}
	
	@Test
	public void testFavoriteSport() {
		Optional<Sport> fav = ed.getFavoriteSport(USERID);
		Assert.assertTrue(fav.isPresent());
		Assert.assertEquals(Sport.SOCCER, fav.get());
	}
	
	@Test
	public void testFavoriteClub() {
		Optional<Club> club = ed.getFavoriteClub(USERID);
		Assert.assertTrue(club.isPresent());
		Assert.assertEquals(CLUBID, club.get().getClubid());
	}
	
	@Rollback
	@Test
	public void testDeleteEvent() {
		ed.deleteEvent(EVENTID);
		Assert.assertEquals(Optional.empty(), ed.findByEventId(EVENTID));
	}

}