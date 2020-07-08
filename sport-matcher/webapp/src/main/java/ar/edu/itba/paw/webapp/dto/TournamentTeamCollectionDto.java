package ar.edu.itba.paw.webapp.dto;

import java.util.List;

public class TournamentTeamCollectionDto {
	
	private List<TournamentTeamDto> teams;
	
	public static TournamentTeamCollectionDto ofTeams(List<TournamentTeamDto> teams) {
		TournamentTeamCollectionDto dto = new TournamentTeamCollectionDto();
		dto.teams = teams;
		
		return dto;
	}

	public List<TournamentTeamDto> getTeams() {
		return teams;
	}

	public void setTeams(List<TournamentTeamDto> teams) {
		this.teams = teams;
	}
}
