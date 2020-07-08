package ar.edu.itba.paw.webapp.dto;

import java.time.Instant;

import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.model.Tournament;

public class TournamentDto {
	
	private long tournamentid;
	private String name;
	private Sport sport;
	private ClubDto tournamentClub;
	private int maxTeams;
	private int teamSize;
	private Instant inscriptionEnd;
	private boolean inscriptionSuccess;
	private Instant createdAt;
	
	public static TournamentDto ofTournament(Tournament tournament) {
		TournamentDto dto = new TournamentDto();
		dto.tournamentid = tournament.getTournamentid();
		dto.name = tournament.getName();
		dto.sport = tournament.getSport();
		dto.tournamentClub = ClubDto.ofClub(tournament.getTournamentClub());
		dto.maxTeams = tournament.getMaxTeams();
		dto.teamSize = tournament.getTeamSize();
		dto.inscriptionEnd = tournament.getEndsInscriptionAt();
		dto.inscriptionSuccess = tournament.getInscriptionSuccess();
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

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

}
