package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.TournamentTeam;

public class TournamentTeamDto {
	
	private long teamid;
	private String teamName;
	private int teamScore;
	
	public static TournamentTeamDto ofTeam(TournamentTeam team) {
		TournamentTeamDto dto = new TournamentTeamDto();
		dto.teamid = team.getTeamid();
		dto.teamName = team.getTeamName();
		dto.teamScore = team.getTeamScore();
		
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
}
