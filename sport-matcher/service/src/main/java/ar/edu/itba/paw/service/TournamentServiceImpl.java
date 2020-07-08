package ar.edu.itba.paw.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
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
import ar.edu.itba.paw.exception.EventHasNotEndedException;
import ar.edu.itba.paw.exception.HourOutOfRangeException;
import ar.edu.itba.paw.exception.IllegalParamException;
import ar.edu.itba.paw.exception.InscriptionClosedException;
import ar.edu.itba.paw.exception.InsufficientPitchesException;
import ar.edu.itba.paw.exception.InvalidTeamAmountException;
import ar.edu.itba.paw.exception.InvalidTeamSizeException;
import ar.edu.itba.paw.exception.MaximumDateExceededException;
import ar.edu.itba.paw.exception.TeamAlreadyFilledException;
import ar.edu.itba.paw.exception.UnevenTeamAmountException;
import ar.edu.itba.paw.exception.UserAlreadyJoinedException;
import ar.edu.itba.paw.exception.UserBusyException;
import ar.edu.itba.paw.interfaces.ClubService;
import ar.edu.itba.paw.interfaces.EmailService;
import ar.edu.itba.paw.interfaces.TournamentDao;
import ar.edu.itba.paw.interfaces.TournamentService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.model.Tournament;
import ar.edu.itba.paw.model.TournamentEvent;
import ar.edu.itba.paw.model.TournamentTeam;
import ar.edu.itba.paw.model.User;

@EnableAsync
@EnableScheduling
@Service
public class TournamentServiceImpl implements TournamentService {
	
	@Autowired
	private TournamentDao td;
	
	@Autowired
	private ClubService cs;
	
	@Autowired
	private UserService us;
	
	@Autowired
    private EmailService ems;
	
	private static final String NEGATIVE_ID_ERROR = "Id must be greater than zero.";
	private static final String NEGATIVE_PAGE_ERROR = "Page number must be greater than zero.";
	private static final String TIME_ZONE = "America/Buenos_Aires";
	private static final int MIN_HOUR = 9;
	private static final int MAX_HOUR = 23;

	private static final int MIN_TEAMS = 4;
	private static final int MAX_TEAMS = 10;
	private static final int MIN_TEAM_SIZE = 3;
	private static final int MAX_TEAM_SIZE = 11;
	private static final int INSCRIPTION_FIRST_ROUND_DAY_DIFFERENCE = 1;
	
	@Override
	public Optional<Tournament> findById(final long tournamentid) {
		if(tournamentid <= 0) {
			throw new IllegalParamException(NEGATIVE_ID_ERROR);
		}
		return td.findById(tournamentid);
	}
	
	@Override
	public List<Tournament> findBy(final int pageNum) {
		if(pageNum <= 0) {
			throw new IllegalParamException(NEGATIVE_PAGE_ERROR);
		}

		return td.findBy(pageNum);
	}
	
	@Override
	public Optional<TournamentTeam> findByTeamId(final long teamid) {
		if(teamid <= 0) {
			throw new IllegalParamException(NEGATIVE_ID_ERROR);
		}

		return td.findByTeamId(teamid);
	}
	
	@Transactional(rollbackFor = { Exception.class })
	@Override
	public Tournament create(final String name, final Sport sport, final Club club, final Integer maxTeams,
			final Integer teamSize, final Instant firstRoundDate, final Integer startsAtHour,
			final Integer endsAtHour, final Instant inscriptionEndDate, final User user) 
					throws DateInPastException, MaximumDateExceededException, EndsBeforeStartsException,
					HourOutOfRangeException, InvalidTeamAmountException, UnevenTeamAmountException,
					InvalidTeamSizeException, InsufficientPitchesException, InscriptionClosedException {
		
    	Instant firstRoundStartsAt = firstRoundDate.plus(startsAtHour, ChronoUnit.HOURS);
    	Instant firstRoundEndsAt = firstRoundDate.plus(endsAtHour, ChronoUnit.HOURS);
    	
    	if(maxTeams < MIN_TEAMS || maxTeams > MAX_TEAMS)
    		throw new InvalidTeamAmountException();
    	if(maxTeams % 2 != 0)
    		throw new UnevenTeamAmountException();
    	if(teamSize < MIN_TEAM_SIZE || teamSize > MAX_TEAM_SIZE)
    		throw new InvalidTeamSizeException();
    	if(inscriptionEndDate.isBefore(Instant.now()))
    		throw new DateInPastException("InscriptionInPast");
    	if(firstRoundStartsAt.compareTo(aWeeksTime().minus(1, ChronoUnit.HOURS)) > 0)
    		throw new MaximumDateExceededException("MaximumStartDateExceeded");
    	if(endsAtHour <= startsAtHour)
    		throw new EndsBeforeStartsException();
    	if(startsAtHour < MIN_HOUR || startsAtHour >= MAX_HOUR || endsAtHour > MAX_HOUR || endsAtHour <= MIN_HOUR)
    		throw new HourOutOfRangeException(MIN_HOUR, MAX_HOUR);
    	if(inscriptionEndDate.isAfter((firstRoundStartsAt.minus(INSCRIPTION_FIRST_ROUND_DAY_DIFFERENCE, ChronoUnit.DAYS))))
    		throw new MaximumDateExceededException("MaximumInscriptionDateExceeded");
    	
    	List<Pitch> availablePitches = cs.getAvailablePitches(club.getClubid(), sport, 
    			firstRoundStartsAt, firstRoundEndsAt, maxTeams/2);
    	if(availablePitches.size() < maxTeams / 2)
    		throw new InsufficientPitchesException();
    		
    	return td.create(name, sport, club, availablePitches, maxTeams, teamSize, firstRoundStartsAt,
    			firstRoundEndsAt, inscriptionEndDate, user);
	}
	
	private Instant today() {
    	return LocalDate.now().atStartOfDay(ZoneId.of(TIME_ZONE)).toInstant();
    }
	
	private Instant aWeeksTime() {
		return today().plus(8, ChronoUnit.DAYS).minus(1, ChronoUnit.HOURS);
	}
	
	@Transactional(rollbackFor = { Exception.class })
	@Override
	public void joinTournament(long tournamentid, long teamid, final long userid) 
			throws UserBusyException, UserAlreadyJoinedException, InscriptionClosedException,
			TeamAlreadyFilledException, EntityNotFoundException {
		if(tournamentid <= 0 || teamid <= 0 || userid <= 0) {
			throw new IllegalParamException(NEGATIVE_ID_ERROR);
		}
		
		final User user = us.findById(userid).orElseThrow(EntityNotFoundException::new);
		
		Tournament tournament = td.findById(tournamentid).orElseThrow(EntityNotFoundException::new);
		if(tournament.getEndsInscriptionAt().compareTo(Instant.now()) <= 0) {
			throw new InscriptionClosedException();
		}
		
		if(td.findUserTeam(tournament, user).isPresent()) {
			throw new UserAlreadyJoinedException();
		}
		
		TournamentTeam team = td.findByTeamId(teamid).orElseThrow(EntityNotFoundException::new);
		if(team.getInscriptions().size() == tournament.getTeamSize() * tournament.getRounds()) {
			throw new TeamAlreadyFilledException();
		}
		
		td.joinTournament(tournament, team, user);
	}

	@Transactional(rollbackFor = { Exception.class })
	@Override
	public void leaveTournament(final long tournamentid, final long userid) 
			throws InscriptionClosedException, EntityNotFoundException {
		Tournament tournament = td.findById(tournamentid).orElseThrow(EntityNotFoundException::new);
		if(tournament.getEndsInscriptionAt().compareTo(Instant.now()) <= 0) {
			throw new InscriptionClosedException();
		}
		User user = us.findById(userid).orElseThrow(EntityNotFoundException::new);
		TournamentTeam team = td.findUserTeam(tournament, user).orElseThrow(EntityNotFoundException::new);
		td.deleteTournamentInscriptions(team, user);
	}
	
	@Transactional(rollbackFor = { Exception.class })
	@Override
	public void kickFromTournament(final User kickedUser, final Tournament tournament) 
			throws InscriptionClosedException, EntityNotFoundException {
		TournamentTeam team = td.findUserTeam(tournament, kickedUser).orElseThrow(EntityNotFoundException::new);
		if(tournament.getEndsInscriptionAt().isBefore(Instant.now()))
			throw new InscriptionClosedException();
		td.deleteTournamentInscriptions(team, kickedUser);
	}
	
	@Override
	public Optional<TournamentTeam> findUserTeam(final long tournamentid, final long userid) throws EntityNotFoundException {
		if(tournamentid <= 0 || userid <= 0) {
			throw new IllegalParamException(NEGATIVE_ID_ERROR);
		}
		Tournament tournament = td.findById(tournamentid).orElseThrow(EntityNotFoundException::new);
		final User user = us.findById(userid).orElseThrow(EntityNotFoundException::new);
		return td.findUserTeam(tournament, user);
	}

	@Override
	public Map<Long, List<User>> mapTeamMembers(long tournamentid) throws EntityNotFoundException {
		if(tournamentid <= 0) {
			throw new IllegalParamException(NEGATIVE_ID_ERROR);
		}
		Tournament tournament = td.findById(tournamentid).orElseThrow(EntityNotFoundException::new);
		Map<Long, List<User>> teamsUsersMap = new TreeMap<>();
		for(TournamentTeam team : tournament.getTeams()) {
			List<User> teamMembers = td.findTeamMembers(team);
			teamsUsersMap.put(team.getTeamid(), teamMembers);
		}
		return teamsUsersMap;
	}
	
	@Override
	public List<User> getTeamMembers(TournamentTeam team) {
		return td.findTeamMembers(team);
	}

	@Override
	public boolean inscriptionEnded(Tournament tournament) {
		return tournament.getEndsInscriptionAt().compareTo(Instant.now()) < 0;
	}

	@Override
	public Map<TournamentTeam, Integer> getTeamsScores(Tournament tournament) {
		Map<TournamentTeam, Integer> teamsScoresMap = new HashMap<>();
		for(TournamentTeam team : tournament.getTeams()) {
			teamsScoresMap.put(team, team.getTeamScore());
		}
		return teamsScoresMap.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).collect(
	            Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
	                    LinkedHashMap::new));
	}

	@Override
	public List<TournamentEvent> findTournamentEventsByRound(final long tournamentid, final int roundPage) throws EntityNotFoundException {
		if(tournamentid <= 0) {
			throw new IllegalParamException(NEGATIVE_ID_ERROR);
		}
		if(roundPage <= 0) {
			throw new IllegalParamException(NEGATIVE_PAGE_ERROR);
		}
		Tournament tournament = td.findById(tournamentid).orElseThrow(EntityNotFoundException::new);
		List<TournamentEvent> events = td.findTournamentEventsByRound(tournament, roundPage);
		Collections.sort(events, new Comparator<TournamentEvent>() {
			@Override
			public int compare(TournamentEvent event1, TournamentEvent event2) {
				return ((Long) event1.getEventId()).compareTo(event2.getEventId());
			}
		});
		return events;
	}

	@Transactional(rollbackFor = { Exception.class })
	@Override
	public void postTournamentEventResult(final Tournament tournament, final long eventid, final Integer firstResult,
			final Integer secondResult) throws EventHasNotEndedException, EntityNotFoundException {
		if(eventid <= 0) {
			throw new IllegalParamException(NEGATIVE_ID_ERROR);
		}
		TournamentEvent event = td.findTournamentEventById(eventid).orElseThrow(EntityNotFoundException::new);
		if(!event.getTournament().equals(tournament)) {
			throw new IllegalParamException("Event " + eventid + " does not belong to tournament " + tournament.getTournamentid());
		}
		if(event.getEndsAt().compareTo(Instant.now()) > 0) {
			throw new EventHasNotEndedException();
		}
		td.postTournamentEventResult(tournament, event, firstResult, secondResult);
	}

	@Override
	public Optional<TournamentEvent> findTournamentEventById(final long eventid) {
		if(eventid <= 0) {
			throw new IllegalParamException(NEGATIVE_ID_ERROR);
		}

		return td.findTournamentEventById(eventid);
	}

	@Override
	public List<User> findTeamMembers(final TournamentTeam team) {
		return td.findTeamMembers(team);
	}

	@Override
	public int getMinRoundForResultInput(final Tournament tournament) {
		for(int i = 1; i <= tournament.getRounds(); i++) {
			for(TournamentEvent te : td.findTournamentEventsByRound(tournament, i)) {
				if(te.getFirstTeamScore() == null || te.getSecondTeamScore() == null) {
					return i;
				}
			}
		}
		return tournament.getRounds();
	}

	@Override
	public int getCurrentRound(Tournament tournament) {
		return td.getCurrentRound(tournament).orElse(tournament.getRounds());
	}

	@Override
	public int getPageInitialTournamentIndex(int pageNum) {
		if(pageNum <= 0) {
			throw new IllegalParamException(NEGATIVE_PAGE_ERROR);
		}
		return td.getPageInitialTournamentIndex(pageNum);
	}

	@Override
	public int countTournamentTotal() {
		return td.countTournamentTotal();
	}

	@Override
	public int countTotalTournamentPages() {
		return td.countTotalTournamentPages();
	}

	@Transactional(rollbackFor = { Exception.class })
	@Override
	public void deleteTournament(final long tournamentid) throws InscriptionClosedException, EntityNotFoundException {
		if(tournamentid <= 0) {
			throw new IllegalParamException(NEGATIVE_ID_ERROR);
		}
		Tournament tournament = findById(tournamentid).orElseThrow(EntityNotFoundException::new);
		if(tournament.getEndsInscriptionAt().isBefore(Instant.now())) {
			throw new InscriptionClosedException();
		}
		td.deleteTournament(tournamentid);
	}
	
	@Async
	@Scheduled(fixedDelay = 1800000) /* Runs every half hour */
	@Transactional(rollbackFor = { Exception.class })
	@Override
	public void checkTournamentInscriptions() throws EntityNotFoundException {
		List<Tournament> inscriptionTournaments = td.getInscriptionProcessTournaments();
		for(Tournament t : inscriptionTournaments) {
			Map<Long, List<User>> teamsMap = mapTeamMembers(t.getTournamentid());
			Optional<Integer> inscriptionCount = td.tournamentUserInscriptionCount(t);
			if(inscriptionCount.isPresent() && inscriptionCount.get() == t.getTeamSize() * t.getMaxTeams()) {
				td.setInscriptionSuccess(t);
				for(List<User> teamMembers : teamsMap.values()) {
					for(User user : teamMembers) {
						ems.tournamentStarted(user, t, LocaleContextHolder.getLocale());
					}
				}
			} else {
				String tournamentName = t.getName();
				td.deleteTournament(t.getTournamentid());
				for(List<User> teamMembers : teamsMap.values()) {
					for(User user : teamMembers) {
						ems.tournamentCancelled(user, tournamentName, LocaleContextHolder.getLocale());
					}
				}
			}
		}
	}

	@Override
	public boolean hasFinished(int rounds, int currentRound, List<TournamentEvent> roundEvents) {
		boolean hasFinished = false;
		if(currentRound == rounds) {
			hasFinished = true;
			for(TournamentEvent event : roundEvents) {
				if(event.getFirstTeamScore() == null) {
					hasFinished = false;
					break;
				}
			}
		}
		return hasFinished;
	}

}
