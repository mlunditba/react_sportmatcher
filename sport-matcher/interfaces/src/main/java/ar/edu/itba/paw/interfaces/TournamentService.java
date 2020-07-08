package ar.edu.itba.paw.interfaces;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import ar.edu.itba.paw.exception.DateInPastException;
import ar.edu.itba.paw.exception.EndsBeforeStartsException;
import ar.edu.itba.paw.exception.EntityNotFoundException;
import ar.edu.itba.paw.exception.EventHasNotEndedException;
import ar.edu.itba.paw.exception.HourOutOfRangeException;
import ar.edu.itba.paw.exception.InscriptionClosedException;
import ar.edu.itba.paw.exception.InsufficientPitchesException;
import ar.edu.itba.paw.exception.InvalidTeamAmountException;
import ar.edu.itba.paw.exception.InvalidTeamSizeException;
import ar.edu.itba.paw.exception.MaximumDateExceededException;
import ar.edu.itba.paw.exception.TeamAlreadyFilledException;
import ar.edu.itba.paw.exception.UnevenTeamAmountException;
import ar.edu.itba.paw.exception.UserAlreadyJoinedException;
import ar.edu.itba.paw.exception.UserBusyException;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.model.Tournament;
import ar.edu.itba.paw.model.TournamentEvent;
import ar.edu.itba.paw.model.TournamentTeam;
import ar.edu.itba.paw.model.User;

public interface TournamentService {
	
	public Optional<Tournament> findById(long tournamentid);
	
	public List<Tournament> findBy(final int pageNum);
	
	public Optional<TournamentTeam> findByTeamId(final long teamid);
	
	public Tournament create(final String name, final Sport sport, final Club club, final Integer maxTeams,
			final Integer teamSize, final Instant firstRoundDate, final Integer startsAt,
			final Integer endsAt, final Instant inscriptionEndDate, final User user) 
					throws DateInPastException, MaximumDateExceededException, EndsBeforeStartsException, 
					HourOutOfRangeException, InvalidTeamAmountException, UnevenTeamAmountException, 
					InvalidTeamSizeException, InsufficientPitchesException, InscriptionClosedException;

	public void joinTournament(final long tournamentid, final long teamid, final long userid) 
			throws UserBusyException, UserAlreadyJoinedException, 
			TeamAlreadyFilledException, InscriptionClosedException, EntityNotFoundException;
	
	public void leaveTournament(final long tournamentid, final long userid) 
			throws InscriptionClosedException, EntityNotFoundException;
	
	public void kickFromTournament(final User kickedUser, final Tournament tournament) throws InscriptionClosedException, EntityNotFoundException;
	
	public Optional<TournamentTeam> findUserTeam(final long tournamentid, final long userid) throws EntityNotFoundException;

	public Map<Long, List<User>> mapTeamMembers(final long tournamentid) throws EntityNotFoundException;

	public boolean inscriptionEnded(final Tournament tournament);

	public Map<TournamentTeam, Integer> getTeamsScores(final Tournament tournament);

	public List<TournamentEvent> findTournamentEventsByRound(final long tournamentid, final int roundPage) throws EntityNotFoundException;

	public void postTournamentEventResult(final Tournament tournament, final long eventid, final Integer firstResult, final Integer secondResult) 
			throws EventHasNotEndedException, EntityNotFoundException;
	
	public Optional<TournamentEvent> findTournamentEventById(final long eventid);

	public List<User> findTeamMembers(final TournamentTeam team);

	public int getMinRoundForResultInput(final Tournament tournament);

	public int getCurrentRound(final Tournament tournament);

	public int getPageInitialTournamentIndex(final int pageNum);

	public int countTournamentTotal();

	public int countTotalTournamentPages();

	public void deleteTournament(final long tournamentid) throws InscriptionClosedException, EntityNotFoundException;
	
	public void checkTournamentInscriptions() throws EntityNotFoundException;

	public boolean hasFinished(final int rounds, final int currentRound, final List<TournamentEvent> roundEvents);

	public List<User> getTeamMembers(final TournamentTeam team);
	
}
