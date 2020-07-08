package ar.edu.itba.paw.service;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.exception.DateInPastException;
import ar.edu.itba.paw.exception.EndsBeforeStartsException;
import ar.edu.itba.paw.exception.EntityNotFoundException;
import ar.edu.itba.paw.exception.EventFullException;
import ar.edu.itba.paw.exception.EventNotFinishedException;
import ar.edu.itba.paw.exception.EventOverlapException;
import ar.edu.itba.paw.exception.HourOutOfRangeException;
import ar.edu.itba.paw.exception.IllegalParamException;
import ar.edu.itba.paw.exception.InscriptionClosedException;
import ar.edu.itba.paw.exception.MaximumDateExceededException;
import ar.edu.itba.paw.exception.UserAlreadyJoinedException;
import ar.edu.itba.paw.exception.UserBusyException;
import ar.edu.itba.paw.exception.UserNotAuthorizedException;
import ar.edu.itba.paw.interfaces.EmailService;
import ar.edu.itba.paw.interfaces.EventDao;
import ar.edu.itba.paw.interfaces.EventService;
import ar.edu.itba.paw.interfaces.InscriptionDao;
import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.model.User;

@Service
@EnableAsync
@EnableScheduling
public class EventServiceImpl implements EventService {

	@Autowired
	private EventDao ed;
	
	@Autowired
	private UserDao ud;
	
	@Autowired
	private InscriptionDao idao;
	
	@Autowired
	private EmailService ems;

	private static final String TIME_ZONE = "America/Buenos_Aires";
	private static final Map<DayOfWeek, Integer> DAYS_OF_WEEK_NUM = new HashMap<>();
	private static final int DAY_LIMIT = 7;
	private static final int MIN_HOUR = 9;
	private static final int MAX_HOUR = 23;
	private static final int INSCRIPTION_END_FROM_EVENT_DAY_DIFFERENCE = 1;
	private static final String NEGATIVE_ID_ERROR = "Id must be greater than zero.";
	private static final String NEGATIVE_PAGE_ERROR = "Page number must be greater than zero.";

	@Override
	public Optional<Event> findByEventId(long eventid) {
		if(eventid <= 0) {
			throw new IllegalParamException(NEGATIVE_ID_ERROR);
		}
		return ed.findByEventId(eventid);
	}
	
	@Override
	public int getMinHour() {
		return MIN_HOUR;
	}
	
	@Override
	public int getMaxHour() {
		return MAX_HOUR;
	}

	@Override
	public List<Event> findByOwner(final boolean futureEvents, final long userid, final int pageNum) {
		if(pageNum <= 0) {
			throw new IllegalParamException(NEGATIVE_PAGE_ERROR);
		}
		return ed.findByOwner(futureEvents, userid, pageNum);
	}
	
	@Override
	public int countByOwner(final boolean futureEvents, final long userid) {
		if(userid <= 0)
			throw new IllegalParamException(NEGATIVE_ID_ERROR);
		return ed.countByOwner(futureEvents, userid);
	}
	
	@Override
	public List<Event> findPastUserInscriptions(final long userid, final int pageNum) {
		if(userid <= 0) {
			throw new IllegalParamException(NEGATIVE_ID_ERROR);
		}
		if(pageNum <= 0) {
			throw new IllegalParamException(NEGATIVE_PAGE_ERROR);
		}
		return ed.findPastUserInscriptions(userid, pageNum);
	}
	
	@Override
	public List<Event> findFutureUserInscriptions(final long userid, final boolean withinWeek) {
		if(userid <= 0) {
			throw new IllegalParamException(NEGATIVE_ID_ERROR);
		}
		return ed.findFutureUserInscriptions(userid, withinWeek);
	}

	@Override
	public List<Event> findCurrentEventsInPitch(final long pitchid) {
		if(pitchid <= 0) {
			throw new IllegalParamException(NEGATIVE_ID_ERROR);
		}
		return ed.findCurrentEventsInPitch(pitchid);
	}

	@Override
	public List<List<Event>> convertEventListToSchedule(List<Event> events) {
		List<List<Event>> schedule = new ArrayList<>();
		for(int i = 0; i < DAY_LIMIT; i++) {
			schedule.add(new ArrayList<Event>());
			for(int j = 0; j < MAX_HOUR - MIN_HOUR; j++) {
				schedule.get(i).add(null);
			}
		}
		int[] indexes = new int [DAY_LIMIT];
		int i = 0;
		for(Event event : events) {
			DayOfWeek startsAtDayOfWeek = event.getStartsAt().atZone(ZoneId.of(TIME_ZONE))
					.toLocalDate().getDayOfWeek();
			DayOfWeek currentDayOfWeek = LocalDate.now(ZoneId.of(TIME_ZONE)).getDayOfWeek();

			Map<DayOfWeek, Integer> daysOfWeek = getDaysOfWeek();

			int dayIndex = (daysOfWeek.get(startsAtDayOfWeek) - daysOfWeek.get(currentDayOfWeek)) % 7;
			if(dayIndex < 0)
				dayIndex += 7;
			
			i = indexes[dayIndex];
			schedule.get(dayIndex).set(i, event);
			indexes[dayIndex]++;
		}
		
		return schedule;
	}	
	
	private Map<DayOfWeek, Integer> getDaysOfWeek() {
		if(DAYS_OF_WEEK_NUM.isEmpty()) {
			int i = 0;
			for(DayOfWeek dow : DayOfWeek.values()) {
				DAYS_OF_WEEK_NUM.put(dow, i);
				i++;
			}
		}
		return DAYS_OF_WEEK_NUM;
	}
	
	@Override
	public List<Event> findByWithInscriptions(boolean onlyFuture, Optional<String> eventName, 
			Optional<String> clubName, Optional<Sport> sport, Optional<String> organizer,
			Optional<Integer> vacancies, Optional<Instant> date, int pageNum) {
		
		List<Event> events = findBy(onlyFuture, eventName, clubName, sport, organizer, 
				vacancies, date, pageNum);
		
		return events;
	}

	@Override
	public Integer countByUserInscriptions(final boolean futureEvents, final long userid) {
		if(userid <= 0)
			throw new IllegalParamException(NEGATIVE_ID_ERROR);
		return ed.countByUserInscriptions(futureEvents, userid);
	}
	
	@Override
	public List<Event> findBy(boolean onlyJoinable, Optional<String> eventName, Optional<String> clubName,
			Optional<Sport> sport, Optional<String> organizer, Optional<Integer> vacancies, 
			Optional<Instant> date, int pageNum) {
		if(pageNum <= 0) {
			throw new IllegalParamException(NEGATIVE_PAGE_ERROR);
		}

		List<Event> events = ed.findBy(onlyJoinable, eventName, clubName, sport.map(Sport::toString), organizer,
				vacancies, date, pageNum);
		
		return events;
	}
	
	@Override
	public Integer countFilteredEvents(final boolean onlyJoinable, final Optional<String> eventName, 
			final Optional<String> clubName, final Optional<Sport> sport, final Optional<String> organizer,
			final Optional<Integer> vacancies, Optional<Instant> date) {
		
		return ed.countFilteredEvents(onlyJoinable, eventName, clubName, 
				sport.map(Sport::toString), organizer, vacancies, date);
	}

	@Override
	public int countFutureEventPages() {
		return ed.countFutureEventPages();
	}

	@Transactional(rollbackFor = { Exception.class })
	@Override
	public Event create(final String name, final User owner, final Pitch pitch,
			final String description, final int maxParticipants, final Instant date, 
			final int startsAtHour, final int endsAtHour, final Instant inscriptionEndDate) 
					throws 	MaximumDateExceededException, EndsBeforeStartsException, 
							EventOverlapException, HourOutOfRangeException, DateInPastException {
		Instant startsAtDate = date.plus(startsAtHour, ChronoUnit.HOURS);

		if(startsAtDate.isBefore(Instant.now()))
			throw new DateInPastException("StartsInPast");
		if(inscriptionEndDate.isBefore(Instant.now()))
			throw new DateInPastException("InscriptionInPast");
    	if(startsAtDate.compareTo(aWeeksTime().minus(1, ChronoUnit.HOURS)) > 0)
    		throw new MaximumDateExceededException("MaximumStartDateExceeded");
    	if(endsAtHour <= startsAtHour)
    		throw new EndsBeforeStartsException();
    	if(startsAtHour < MIN_HOUR || startsAtHour >= MAX_HOUR || endsAtHour > MAX_HOUR || endsAtHour <= MIN_HOUR)
    		throw new HourOutOfRangeException(MIN_HOUR, MAX_HOUR);
    	if(inscriptionEndDate.isAfter((startsAtDate.minus(INSCRIPTION_END_FROM_EVENT_DAY_DIFFERENCE, ChronoUnit.DAYS))))
    		throw new MaximumDateExceededException("MaximumInscriptionDateExceeded");

		return ed.create(name, owner, pitch, description, maxParticipants, 
				startsAtDate, date.plus(endsAtHour, ChronoUnit.HOURS), inscriptionEndDate);
	}
	
	private Instant today() {
    	return LocalDate.now().atStartOfDay(ZoneId.of(TIME_ZONE)).toInstant();
    }
	
	private Instant aWeeksTime() {
		return today().plus(8, ChronoUnit.DAYS).minus(1, ChronoUnit.HOURS);
	}

	@Transactional(rollbackFor = { Exception.class })
	@Override
	public void joinEvent(final long userid, final long eventid)
			throws UserAlreadyJoinedException, EventFullException, UserBusyException, InscriptionClosedException, EntityNotFoundException {

		if(userid <= 0 || eventid <= 0)
			throw new IllegalParamException(NEGATIVE_ID_ERROR);
		
		final Event event = ed.findByEventId(eventid).orElseThrow(EntityNotFoundException::new);
		final User user = ud.findById(userid).orElseThrow(EntityNotFoundException::new);
		
		if(event.getEndsInscriptionAt().isBefore(Instant.now())) {
			throw new InscriptionClosedException();
		}
		
		if(countParticipants(event.getEventId()) + 1 > event.getMaxParticipants()) {
			throw new EventFullException();
		}

		ed.joinEvent(user, event);
	}

	@Transactional(rollbackFor = { Exception.class })
	@Override
	public void leaveEvent(final long eventid, final long userid) throws DateInPastException, EntityNotFoundException {
		final Event event = ed.findByEventId(eventid).orElseThrow(EntityNotFoundException::new);
		ud.findById(userid).orElseThrow(EntityNotFoundException::new);
		if(event.getEndsInscriptionAt().isBefore(Instant.now())) {
			throw new DateInPastException("InscriptionClosed");
		}
		idao.deleteInscription(eventid, userid);
	}

	@Transactional(rollbackFor = { Exception.class })
	@Override
	public void kickFromEvent(final User owner, final long kickedUserId, final Event event)
		throws UserNotAuthorizedException, DateInPastException, EntityNotFoundException {
		ud.findById(kickedUserId).orElseThrow(EntityNotFoundException::new);
		if(owner.getUserid() != event.getOwner().getUserid())
			throw new UserNotAuthorizedException("User is not the owner of the event.");
		if(owner.getUserid() == kickedUserId)
			throw new UserNotAuthorizedException("Owner cannot be kicked from the event. Must leave instead.");
		if(event.getEndsInscriptionAt().isBefore(Instant.now())) {
			throw new DateInPastException("InscriptionClosed");
		}
		idao.deleteInscription(event.getEventId(), kickedUserId);
	}

	@Override
	public int countParticipants(final long eventid) {
		if(eventid <= 0) {
			throw new IllegalParamException(NEGATIVE_ID_ERROR);
		}
		return ed.countParticipants(eventid);
	}

	@Override
	public Optional<Sport> getFavoriteSport(final long userid) {
		return ed.getFavoriteSport(userid);
	}

	@Override
	public Optional<Club> getFavoriteClub(final long userid) {
		return ed.getFavoriteClub(userid);
	}
	
	@Override
	public int getPageInitialEventIndex(final int pageNum) {
		if(pageNum <= 0) {
			throw new IllegalParamException(NEGATIVE_PAGE_ERROR);
		}
		return ed.getPageInitialEventIndex(pageNum);
	}
	
	@Transactional(rollbackFor = { Exception.class })
	@Override
	public void deleteEvent(long eventid) throws DateInPastException, EntityNotFoundException {
		if(eventid <= 0) {
			throw new IllegalParamException(NEGATIVE_ID_ERROR);
		}
		final Event event = ed.findByEventId(eventid).orElseThrow(EntityNotFoundException::new);
		if(event.getStartsAt().isBefore(Instant.now())) {
			throw new DateInPastException("EventStarted");
		}
		ed.deleteEvent(eventid);
	}
	
	@Transactional(rollbackFor = { Exception.class })
	@Override
	public void cancelEvent(final Event event, final long userid) 
			throws UserNotAuthorizedException, DateInPastException {
		if(event.getEventId() <= 0 || userid <= 0) {
			throw new IllegalParamException(NEGATIVE_ID_ERROR);
		}
		if(event.getOwner().getUserid() != userid) {
			throw new UserNotAuthorizedException("Cannot cancel event if now the owner");
		}
		if(event.getEndsInscriptionAt().isBefore(Instant.now())) {
			throw new DateInPastException("InscriptionClosed");
		}
		ed.deleteEvent(event.getEventId());
	}

	@Override
	public int getVoteBalance(final long eventid) {
		return idao.getVoteBalance(eventid).orElse(0);
	}

	@Override
	public int getUserVote(final long eventid, final long userid) {
		return idao.getUserVote(eventid, userid).orElse(0);
	}

	@Transactional(rollbackFor = { Exception.class })
	@Override
	public void vote(final boolean isUpvote, final Event event, final long userid)
		throws UserNotAuthorizedException, EventNotFinishedException {
		if(event.getEndsAt().isAfter(Instant.now()))
			throw new EventNotFinishedException("User cannot vote if event has not finished.");
		if(event.getOwner().getUserid() == userid)
			throw new UserNotAuthorizedException("User cannot vote for themselves.");
		int changed = idao.vote(isUpvote, event.getEventId(), userid);
		if(changed == 0)
			throw new UserNotAuthorizedException("User cannot vote if no inscription is present.");
	}

	@Override
	public int countUserInscriptionPages(final boolean onlyFuture, final long userid) {
		if(userid <= 0)
			throw new IllegalParamException(NEGATIVE_ID_ERROR);
		return ed.countUserInscriptionPages(onlyFuture, userid);
	}
	
	@Override
	public int countUserOwnedPages(final boolean onlyFuture, final long userid) {
		if(userid <= 0)
			throw new IllegalParamException(NEGATIVE_ID_ERROR);
		return ed.countUserOwnedPages(onlyFuture, userid);
	}
	
	@Async
	@Scheduled(fixedDelay = 1800000) /* Runs every half hour */
	@Transactional(rollbackFor = { Exception.class })
	@Override
	public void checkUncompletedEvents() {
		List<Event> inscriptionEvents = ed.getEndedInscriptionProcessingEvents();
		for(Event event : inscriptionEvents) {
			List<User> inscriptedUsers = event.getInscriptions().stream().map(i -> i.getInscriptedUser()).collect(Collectors.toList());
			if(event.getInscriptions().size() == event.getMaxParticipants()) {
				ed.setInscriptionSuccess(event);
				for(User user : inscriptedUsers) {
					ems.eventStarted(user, event, LocaleContextHolder.getLocale());
				}
				if(!inscriptedUsers.contains(event.getOwner()))
					ems.eventStarted(event.getOwner(), event, LocaleContextHolder.getLocale());
			} else {
				String eventName = event.getName();
				ed.deleteEvent(event.getEventId());
				for(User user : inscriptedUsers) {
					ems.eventCancelled(user, eventName, LocaleContextHolder.getLocale());
				}
				if(!inscriptedUsers.contains(event.getOwner()))
					ems.eventCancelled(event.getOwner(), eventName, LocaleContextHolder.getLocale());
			}
		}
	}

	@Override
	public int countEventPages(final int totalEventQty) {
		return ed.countEventPages(totalEventQty);
	}

}
