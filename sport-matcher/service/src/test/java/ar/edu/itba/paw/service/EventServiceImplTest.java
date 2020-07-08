package ar.edu.itba.paw.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ContextConfiguration;

import ar.edu.itba.paw.exception.DateInPastException;
import ar.edu.itba.paw.exception.EndsBeforeStartsException;
import ar.edu.itba.paw.exception.EventNotFinishedException;
import ar.edu.itba.paw.exception.HourOutOfRangeException;
import ar.edu.itba.paw.exception.MaximumDateExceededException;
import ar.edu.itba.paw.interfaces.EventDao;
import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.User;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class EventServiceImplTest {
	
	@Mock
	private EventDao ed;
	
	@Mock
	private UserDao ud;
	
	@InjectMocks
	private EventServiceImpl es;
	
	private static final User USER = Mockito.mock(User.class);
	private static final Pitch PITCH = Mockito.mock(Pitch.class);
	
	private static final String NAME_1 = "e1";
	private static final String NAME_2 = "e1";
	private static final String DESCRIPTION = "d";
	private static final String TIME_ZONE = "America/Buenos_Aires";
	
	private static final Instant STARTS_1 =  today().toInstant().plus(2, ChronoUnit.DAYS);
	private static final Instant ENDS_1 =  today()
			.plus(1, ChronoUnit.DAYS).toInstant();
	private static final Instant STARTS_2 =  today().
			plus(1, ChronoUnit.DAYS).toInstant();
	private static final Instant ENDS_2 =  today().plus(1, ChronoUnit.DAYS)
			.plus(1, ChronoUnit.HOURS).toInstant();
	private static final Instant NOW = Instant.now();
	private static final Event EVENT_1 = new Event(NAME_1, PITCH, DESCRIPTION, 2, STARTS_1, ENDS_1, STARTS_1);
	private static final Event EVENT_2 = new Event(NAME_2, PITCH, DESCRIPTION, 2, STARTS_2, ENDS_2, NOW);
	private static final List<Event> EVENT_LIST = new ArrayList<>();
			
	private static ZonedDateTime today() {
		return LocalDate.now().atStartOfDay(ZoneId.of(TIME_ZONE));
	}
	
	@Before
	public void setUp() {
		EVENT_LIST.add(EVENT_1);
		EVENT_LIST.add(EVENT_2);
	}
	
	@Test
	public void validCreateTest() throws Exception {
		Mockito.when(ed.create(
				Mockito.anyString(),
				Mockito.any(User.class),
				Mockito.any(Pitch.class),
				Mockito.anyString(),
				Mockito.anyInt(),
				Mockito.any(Instant.class),
				Mockito.any(Instant.class),
				Mockito.any(Instant.class)))
		.thenReturn(EVENT_1);
		final Event e = es.create(NAME_1, USER, PITCH, DESCRIPTION,
				1, NOW.plus(2, ChronoUnit.DAYS),
				10, 11, NOW.plus(1, ChronoUnit.HOURS));
		Assert.assertEquals(EVENT_1, e);
	}
	
	@Test(expected = DateInPastException.class)
	public void createWithPastDate() throws Exception {
		es.create(NAME_1, USER, PITCH, DESCRIPTION,
				1, NOW.minus(2, ChronoUnit.DAYS),
				10, 11, NOW.minus(4, ChronoUnit.DAYS));
	}
	
	@Test(expected = MaximumDateExceededException.class)
	public void createEventInEightDays() throws Exception {
		es.create(NAME_1, USER, PITCH, DESCRIPTION,
				1, NOW.plus(8, ChronoUnit.DAYS),
				10, 11, NOW.plus(5, ChronoUnit.DAYS));
	}

	@Test(expected = EndsBeforeStartsException.class)
	public void createEndsBeforeStartsTest() throws Exception {
		es.create(NAME_1, USER, PITCH, DESCRIPTION,
				1, NOW.plus(3, ChronoUnit.DAYS),
				20, 15, NOW.plus(1, ChronoUnit.DAYS));
	}
	
	@Test(expected = HourOutOfRangeException.class)
	public void createDisrespectsPitchHours() throws Exception {
		es.create(NAME_1, USER, PITCH, DESCRIPTION,
				1, NOW.plus(3, ChronoUnit.DAYS),
				2 /*AM*/, 9 /*AM*/, NOW.plus(1, ChronoUnit.DAYS));
	}
	
	@Test(expected = EventNotFinishedException.class)
	public void voteTest() throws Exception {
		Event ev = new Event(NAME_2, PITCH, DESCRIPTION, 2, 
				NOW.minus(1, ChronoUnit.HOURS), NOW.plus(1, ChronoUnit.HOURS), NOW);
		es.vote(true, ev, 1);
	}
	
}
