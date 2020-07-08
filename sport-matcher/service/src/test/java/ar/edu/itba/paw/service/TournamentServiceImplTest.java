package ar.edu.itba.paw.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ContextConfiguration;

import ar.edu.itba.paw.exception.InscriptionClosedException;
import ar.edu.itba.paw.exception.InsufficientPitchesException;
import ar.edu.itba.paw.exception.MaximumDateExceededException;
import ar.edu.itba.paw.interfaces.ClubService;
import ar.edu.itba.paw.interfaces.TournamentDao;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.model.Tournament;
import ar.edu.itba.paw.model.User;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class TournamentServiceImplTest {
	
	@InjectMocks
	private TournamentServiceImpl ts;
	
	@Mock
	private TournamentDao td;
	
	@Mock
	private ClubService cd;
	
	@Mock
	private UserService us;
	
	private static final String TIME_ZONE = "America/Buenos_Aires";
	private static final String TNAME = "Tournament";
	private static final Sport SPORT = Sport.SOCCER;
	private static final Club CLUB = Mockito.mock(Club.class);
	private static final List<Pitch> PITCHES = new ArrayList<>();
	private static final int MAX_TEAMS = 4;
	private static final int TEAM_SIZE = 3;
	private static final Instant STARTS = today().toInstant().plus(2, ChronoUnit.DAYS)
			.plus(10, ChronoUnit.HOURS);
	private static final int STARTS_AT = 10;
	private static final int ENDS_AT = 12;
	private static final Instant INSCR_ENDS = Instant.now().plus(5, ChronoUnit.HOURS);
	private static final User USER = Mockito.mock(User.class);
	private static final Tournament TMOCK = Mockito.mock(Tournament.class);
	
	private static ZonedDateTime today() {
		return LocalDate.now().atStartOfDay(ZoneId.of(TIME_ZONE));
	}
	
	@Before
	public void setUp() {
		for(int i = 0; i < 3; i++) {
			PITCHES.add(Mockito.mock(Pitch.class));
		}
		Mockito.when(cd.getAvailablePitches(
				Mockito.anyLong(),
				Mockito.any(Sport.class),
				Mockito.any(Instant.class),
				Mockito.any(Instant.class),
				Mockito.anyInt())).thenReturn(PITCHES);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void createTournamentTest() throws Exception {
		Mockito.when(td.create(
				Mockito.anyString(),
				Mockito.any(Sport.class),
				Mockito.any(Club.class),
				Mockito.any(List.class),
				Mockito.anyInt(),
				Mockito.anyInt(),
				Mockito.any(Instant.class),
				Mockito.any(Instant.class),
				Mockito.any(Instant.class),
				Mockito.any(User.class)
			)
		).thenReturn(TMOCK);
		final Tournament t = ts.create(
				TNAME,
				SPORT,
				CLUB,
				MAX_TEAMS,
				TEAM_SIZE,
				STARTS,
				STARTS_AT,
				ENDS_AT,
				INSCR_ENDS,
				USER
		);
		Assert.assertEquals(TMOCK, t);
	}
	
	@Test(expected = InscriptionClosedException.class)
	public void createWithPastDateTest() throws Exception {
		ts.create(
				TNAME,
				SPORT,
				CLUB,
				MAX_TEAMS,
				TEAM_SIZE,
				STARTS,
				STARTS_AT,
				ENDS_AT,
				Instant.now().minus(2, ChronoUnit.DAYS),
				USER
		);
	}
	
	@Test(expected = MaximumDateExceededException.class)
	public void createWithMaximumDateExceededTest() throws Exception {
		ts.create(
				TNAME,
				SPORT,
				CLUB,
				MAX_TEAMS,
				TEAM_SIZE,
				Instant.now().plus(10, ChronoUnit.DAYS),
				STARTS_AT,
				ENDS_AT,
				Instant.now(),
				USER
		);
	}
	
	@Test(expected = InsufficientPitchesException.class)
	public void createWithoutPitchesTest() throws Exception {
		PITCHES.removeAll(PITCHES);
		ts.create(
				TNAME,
				SPORT,
				CLUB,
				MAX_TEAMS,
				TEAM_SIZE,
				STARTS,
				STARTS_AT,
				ENDS_AT,
				INSCR_ENDS,
				USER
		);
	}
	
	@Test(expected = InscriptionClosedException.class)
	public void joinTournamentPreviousInsscrDateTest() throws Exception {
		Mockito.when(us.findById(Mockito.anyLong())).thenReturn(Optional.of(USER));
		Mockito.when(td.findById(Mockito.anyLong())).thenReturn(Optional.of(
			new Tournament(TNAME, SPORT, CLUB, MAX_TEAMS, TEAM_SIZE, Instant.now())));
		ts.joinTournament(1, 1, 1);
	}

}
