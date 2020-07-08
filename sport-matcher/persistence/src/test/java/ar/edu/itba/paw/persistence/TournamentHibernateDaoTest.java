package ar.edu.itba.paw.persistence;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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

import ar.edu.itba.paw.interfaces.TournamentDao;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.Inscription;
import ar.edu.itba.paw.model.InscriptionId;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.model.Tournament;
import ar.edu.itba.paw.model.TournamentEvent;
import ar.edu.itba.paw.model.TournamentTeam;
import ar.edu.itba.paw.model.User;

@Sql("classpath:schema.sql")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class TournamentHibernateDaoTest {
	
	@PersistenceContext
	private EntityManager em;

	@Autowired
	private TournamentDao td;
	
	private static final long TOURNAMENTID = 1;
	private static final String TOURNAMENT_NAME = "tournament";
	private static final Sport TOURNAMENT_SPORT = Sport.SOCCER;
	private static final long TOURNAMENT_CLUBID = 1;
	private static final int TOURNAMENT_MAXTEAMS = 4;
	private static final int TOURNAMENT_TEAMSIZE = 3;
	private static final Timestamp INSCRIPTION_ENDS = Timestamp.valueOf("2030-01-20 11:00:00");
	private static final boolean INSCRIPTION_SUCCESS = false;
	
	private static final long FIRST_PITCHID = 1;
	private static final long SECOND_PITCHID = 2;
	
	private static final long USERID = 1;
	private static final long FIRST_TOURNAMENT_EVENT_ID = 3;
	private static final String FIRST_TOURNAMENT_EVENT_NAME = "tournament R1";
	private static final long SECOND_TOURNAMENT_EVENT_ID = 5;
	private static final long THIRD_TOURNAMENT_EVENT_ID = 7;
	private static final Instant FIRST_ROUND_STARTS_AT = Instant.now().plus(5, ChronoUnit.DAYS);
	private static final Instant INSCRIPTION_ENDS_AT = FIRST_ROUND_STARTS_AT.minus(2, ChronoUnit.DAYS);
	private static final int DURATION = 1;
	private static final Instant FIRST_ROUND_ENDS_AT = FIRST_ROUND_STARTS_AT.plus(DURATION, ChronoUnit.HOURS);
	
	private static final long TEAMID = 1;
	private static final String TEAM_NAME = "team_1";
	private static final int TEAM_SCORE = 0;
	
	private static final InscriptionId FIRST_ROUND_INSCRIPTION_ID = new InscriptionId(FIRST_TOURNAMENT_EVENT_ID, USERID);
	private static final InscriptionId SECOND_ROUND_INSCRIPTION_ID = new InscriptionId(SECOND_TOURNAMENT_EVENT_ID, USERID);
	private static final InscriptionId THIRD_ROUND_INSCRIPTION_ID = new InscriptionId(THIRD_TOURNAMENT_EVENT_ID, USERID);
	
	/* Parameters for a User who has joined a Tournament */
	private static final long SECOND_USERID = 2;
	private static final long SECOND_USER_TEAMID = 4;
	private static final long SECOND_USER_FIRST_TOURNAMENT_EVENT_ID = 4;
	private static final long SECOND_USER_SECOND_TOURNAMENT_EVENT_ID = 5;
	private static final long SECOND_USER_THIRD_TOURNAMENT_EVENT_ID = 8;
	private static final InscriptionId SECOND_USER_FIRST_ROUND_INSCRIPTION_ID = new InscriptionId(SECOND_USER_FIRST_TOURNAMENT_EVENT_ID, SECOND_USERID);
	private static final InscriptionId SECOND_USER_SECOND_ROUND_INSCRIPTION_ID = new InscriptionId(SECOND_USER_SECOND_TOURNAMENT_EVENT_ID, SECOND_USERID);
	private static final InscriptionId SECOND_USER_THIRD_ROUND_INSCRIPTION_ID = new InscriptionId(SECOND_USER_THIRD_TOURNAMENT_EVENT_ID, SECOND_USERID);
	
	private static final long THIRD_TEAM_ID = 3;
	
	private static final long ADMINID = 4;
	
	@Test
	public void testFindById() {
		final Optional<Tournament> tournament = td.findById(TOURNAMENTID);
		Assert.assertTrue(tournament.isPresent());
		Assert.assertEquals(TOURNAMENT_NAME, tournament.get().getName());
		Assert.assertEquals(TOURNAMENT_SPORT, tournament.get().getSport());
		Assert.assertEquals(TOURNAMENT_CLUBID, tournament.get().getTournamentClub().getClubid());
		Assert.assertEquals(TOURNAMENT_MAXTEAMS, tournament.get().getMaxTeams());
		Assert.assertEquals(TOURNAMENT_TEAMSIZE, tournament.get().getTeamSize());
		Assert.assertEquals(INSCRIPTION_ENDS.toInstant(), tournament.get().getEndsInscriptionAt());
		Assert.assertEquals(INSCRIPTION_SUCCESS, tournament.get().getInscriptionSuccess());
		Assert.assertNotNull(tournament.get().getCreatedAt());
	}
	
	@Test
	public void testFindByTeamId() {
		final Optional<TournamentTeam> team = td.findByTeamId(TEAMID);
		Assert.assertTrue(team.isPresent());
		Assert.assertEquals(TEAM_NAME, team.get().getTeamName());
		Assert.assertEquals(TOURNAMENTID, team.get().getTournament().getTournamentid());
		Assert.assertEquals(TEAM_SCORE, team.get().getTeamScore());
	}
	
	@Rollback
	@Test
	public void testCreateTournament() throws Exception {
		List<Pitch> availablePitches = new ArrayList<>();
		availablePitches.add(em.find(Pitch.class, FIRST_PITCHID));
		availablePitches.add(em.find(Pitch.class, SECOND_PITCHID));
		final Tournament tournament = td.create(TOURNAMENT_NAME, TOURNAMENT_SPORT, em.find(Club.class, TOURNAMENT_CLUBID), 
				availablePitches, TOURNAMENT_MAXTEAMS, TOURNAMENT_TEAMSIZE, FIRST_ROUND_STARTS_AT, FIRST_ROUND_ENDS_AT,
				INSCRIPTION_ENDS_AT, em.find(User.class, ADMINID));
		
		Assert.assertNotNull(tournament);
		Assert.assertEquals(TOURNAMENT_NAME, tournament.getName());
		Assert.assertEquals(TOURNAMENT_SPORT, tournament.getSport());
		Assert.assertEquals(TOURNAMENT_CLUBID, tournament.getTournamentClub().getClubid());
		Assert.assertEquals(TOURNAMENT_MAXTEAMS, tournament.getMaxTeams());
		Assert.assertEquals(TOURNAMENT_TEAMSIZE, tournament.getTeamSize());
		Assert.assertEquals(INSCRIPTION_ENDS_AT, tournament.getEndsInscriptionAt());
		Assert.assertNotNull(tournament.getCreatedAt());
	}
	
	@Rollback
	@Test
	public void testJoinTournament() throws Exception {
		td.joinTournament(em.find(Tournament.class, TOURNAMENTID), em.find(TournamentTeam.class, TEAMID), em.find(User.class, USERID));
		Assert.assertNotNull(em.find(Inscription.class, FIRST_ROUND_INSCRIPTION_ID));
		Assert.assertNotNull(em.find(Inscription.class, SECOND_ROUND_INSCRIPTION_ID));
		Assert.assertNotNull(em.find(Inscription.class, THIRD_ROUND_INSCRIPTION_ID));
		Assert.assertEquals(TEAMID, em.find(Inscription.class, FIRST_ROUND_INSCRIPTION_ID).getTournamentTeam().getTeamid());
		Assert.assertEquals(TEAMID, em.find(Inscription.class, SECOND_ROUND_INSCRIPTION_ID).getTournamentTeam().getTeamid());
		Assert.assertEquals(TEAMID, em.find(Inscription.class, THIRD_ROUND_INSCRIPTION_ID).getTournamentTeam().getTeamid());
	}
	
	@Rollback
	@Test
	public void testDeleteTournamentInscriptions() {
		td.deleteTournamentInscriptions(em.find(TournamentTeam.class, SECOND_USER_TEAMID), em.find(User.class, SECOND_USERID));
		Assert.assertNull(em.find(Inscription.class, SECOND_USER_FIRST_ROUND_INSCRIPTION_ID));
		Assert.assertNull(em.find(Inscription.class, SECOND_USER_SECOND_ROUND_INSCRIPTION_ID));
		Assert.assertNull(em.find(Inscription.class, SECOND_USER_THIRD_ROUND_INSCRIPTION_ID));
	}
	
	@Test
	public void testFindUserTeam() {
		Optional<TournamentTeam> team = td.findUserTeam(em.find(Tournament.class, TOURNAMENTID), em.find(User.class, SECOND_USERID));
		Assert.assertTrue(team.isPresent());
		Assert.assertEquals(SECOND_USER_TEAMID, team.get().getTeamid());
	}
	
	@Test
	public void testFindTournamentEventsByTeam() {
		List<TournamentEvent> eventsByTeam = td.findTournamentEventsByTeam(em.find(Tournament.class, TOURNAMENTID), em.find(TournamentTeam.class, USERID));
		List<TournamentEvent> expectedEventsByTeam = new ArrayList<>();
		expectedEventsByTeam.add(em.find(TournamentEvent.class, FIRST_TOURNAMENT_EVENT_ID));
		expectedEventsByTeam.add(em.find(TournamentEvent.class, SECOND_TOURNAMENT_EVENT_ID));
		expectedEventsByTeam.add(em.find(TournamentEvent.class, THIRD_TOURNAMENT_EVENT_ID));
		Assert.assertEquals(eventsByTeam, expectedEventsByTeam);
	}
	
	@Test
	public void testFindTournamentEventsByRound() {
		List<TournamentEvent> tournamentEvents = td.findTournamentEventsByRound(em.find(Tournament.class, TOURNAMENTID), 1);
		List<TournamentEvent> expectedTournamentEvents = new ArrayList<>();
		expectedTournamentEvents.add(em.find(TournamentEvent.class, FIRST_TOURNAMENT_EVENT_ID));
		expectedTournamentEvents.add(em.find(TournamentEvent.class, SECOND_USER_FIRST_TOURNAMENT_EVENT_ID));
		Assert.assertEquals(tournamentEvents, expectedTournamentEvents);
	}
	
	@Test
	public void testFindTournamentEventById() {
		Optional<TournamentEvent> tournamentEvent = td.findTournamentEventById(FIRST_TOURNAMENT_EVENT_ID);
		Assert.assertTrue(tournamentEvent.isPresent());
		Assert.assertEquals(ADMINID, tournamentEvent.get().getOwner().getUserid());
		Assert.assertEquals(FIRST_PITCHID, tournamentEvent.get().getPitch().getPitchid());
		Assert.assertEquals(FIRST_TOURNAMENT_EVENT_NAME, tournamentEvent.get().getName());
		Assert.assertEquals(TEAMID, tournamentEvent.get().getFirstTeam().getTeamid());
		Assert.assertEquals(THIRD_TEAM_ID, tournamentEvent.get().getSecondTeam().getTeamid());
		Assert.assertNotNull(tournamentEvent.get().getCreatedAt());
	}
	
	@Test
	public void testTournamentUserInscriptionCount() {
		Optional<Integer> inscriptions = td.tournamentUserInscriptionCount(em.find(Tournament.class, TOURNAMENTID));
		Assert.assertTrue(inscriptions.isPresent());
		Assert.assertEquals((Integer) 1, inscriptions.get());
	}

}
