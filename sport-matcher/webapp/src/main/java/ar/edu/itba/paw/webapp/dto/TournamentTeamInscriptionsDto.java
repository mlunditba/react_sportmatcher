package ar.edu.itba.paw.webapp.dto;

import java.util.List;

import ar.edu.itba.paw.model.TournamentTeam;

public class TournamentTeamInscriptionsDto {
	
	private long teamid;
	private String teamName;
	private int teamScore;
	private List<UserDto> inscriptions;
	
	public static TournamentTeamInscriptionsDto ofTeam(TournamentTeam team, List<UserDto> inscriptions) {
		TournamentTeamInscriptionsDto dto = new TournamentTeamInscriptionsDto();
		dto.teamid = team.getTeamid();
		dto.teamName = team.getTeamName();
		dto.teamScore = team.getTeamScore();
		dto.inscriptions = inscriptions;
		
		return dto;
	}

	public long getTeamid() {
		return teamid;
	}

	public void setTeamid(long teamid) {
		this.teamid = teamid;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public int getTeamScore() {
		return teamScore;
	}

	public void setTeamScore(int teamScore) {
		this.teamScore = teamScore;
	}

	public List<UserDto> getInscriptions() {
		return inscriptions;
	}

	public void setInscriptions(List<UserDto> inscriptions) {
		this.inscriptions = inscriptions;
	}

}
