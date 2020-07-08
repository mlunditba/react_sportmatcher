package ar.edu.itba.paw.webapp.dto;

import java.time.Instant;

import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.model.Tournament;

public class FullTournamentDto {
	
	private long tournamentid;
	private String name;
	private Sport sport;
	private ClubDto tournamentClub;
	private int maxTeams;
	private int rounds;
	private int teamSize;
	private Instant inscriptionEnd;
	private boolean inscriptionSuccess;
	private Instant startsAt;
	private Instant createdAt;
	
	public static FullTournamentDto ofTournament(Tournament tournament, int rounds, Instant startsAt) {
		FullTournamentDto dto = new FullTournamentDto();
		dto.tournamentid = tournament.getTournamentid();
		dto.name = tournament.getName();
		dto.sport = tournament.getSport();
		dto.tournamentClub = ClubDto.ofClub(tournament.getTournamentClub());
		dto.maxTeams = tournament.getMaxTeams();
		dto.rounds = rounds;
		dto.teamSize = tournament.getTeamSize();
		dto.inscriptionEnd = tournament.getEndsInscriptionAt();
		dto.inscriptionSuccess = tournament.getInscriptionSuccess();
		dto.startsAt = startsAt;
		dto.createdAt = tournament.getCreatedAt();
		
		return dto;
	}

	public long getTournamentid() {
		return tournamentid;
	}

	public void setTournamentid(long tournamentid) {
		this.tournamentid = tournamentid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Sport getSport() {
		return sport;
	}

	public void setSport(Sport sport) {
		this.sport = sport;
	}

	public ClubDto getTournamentClub() {
		return tournamentClub;
	}

	public void setTournamentClub(ClubDto tournamentClub) {
		this.tournamentClub = tournamentClub;
	}

	public int getMaxTeams() {
		return maxTeams;
	}

	public void setMaxTeams(int maxTeams) {
		this.maxTeams = maxTeams;
	}

	public int getRounds() {
		return rounds;
	}

	public void setRounds(int rounds) {
		this.rounds = rounds;
	}

	public int getTeamSize() {
		return teamSize;
	}

	public void setTeamSize(int teamSize) {
		this.teamSize = teamSize;
	}

	public Instant getInscriptionEnd() {
		return inscriptionEnd;
	}

	public void setInscriptionEnd(Instant inscriptionEnd) {
		this.inscriptionEnd = inscriptionEnd;
	}

	public boolean isInscriptionSuccess() {
		return inscriptionSuccess;
	}

	public void setInscriptionSuccess(boolean inscriptionSuccess) {
		this.inscriptionSuccess = inscriptionSuccess;
	}

	public Instant getStartsAt() {
		return startsAt;
	}

	public void setStartsAt(Instant startsAt) {
		this.startsAt = startsAt;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

}
