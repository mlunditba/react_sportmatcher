package ar.edu.itba.paw.webapp.dto;

import java.util.List;

public class TournamentTeamInscriptionsCollectionDto {
	
	private List<TournamentTeamInscriptionsDto> teams;
	private boolean hasJoined;
	
	public static TournamentTeamInscriptionsCollectionDto ofTeams(List<TournamentTeamInscriptionsDto> teams, 
			boolean hasJoined) {
		TournamentTeamInscriptionsCollectionDto dto = new TournamentTeamInscriptionsCollectionDto();
		dto.teams = teams;
		dto.hasJoined = hasJoined;
		
		return dto;
	}

	public List<TournamentTeamInscriptionsDto> getTeams() {
		return teams;
	}

	public void setTeams(List<TournamentTeamInscriptionsDto> teams) {
		this.teams = teams;
	}

	public boolean isHasJoined() {
		return hasJoined;
	}

	public void setHasJoined(boolean hasJoined) {
		this.hasJoined = hasJoined;
	}

}
