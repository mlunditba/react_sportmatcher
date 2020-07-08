package ar.edu.itba.paw.webapp.dto;

import java.util.List;

public class TournamentCollectionDto {
	
	private List<TournamentDto> tournaments;
	private int tournamentCount;
	private int lastPageNum;
	private int initialPageIndex;
	
	public static TournamentCollectionDto ofTournaments(List<TournamentDto> tournaments, int tournamentCount, 
			int lastPageNum, int initialPageIndex) {
		TournamentCollectionDto dto = new TournamentCollectionDto();
		dto.tournaments = tournaments;
		dto.tournamentCount = tournamentCount;
		dto.lastPageNum = lastPageNum;
		dto.initialPageIndex = initialPageIndex;
		
		return dto;
	}

	public List<TournamentDto> getTournaments() {
		return tournaments;
	}

	public void setTournaments(List<TournamentDto> tournaments) {
		this.tournaments = tournaments;
	}

	public int getTournamentCount() {
		return tournamentCount;
	}

	public void setTournamentCount(int tournamentCount) {
		this.tournamentCount = tournamentCount;
	}

	public int getLastPageNum() {
		return lastPageNum;
	}

	public void setLastPageNum(int lastPageNum) {
		this.lastPageNum = lastPageNum;
	}

	public int getInitialPageIndex() {
		return initialPageIndex;
	}

	public void setInitialPageIndex(int initialPageIndex) {
		this.initialPageIndex = initialPageIndex;
	}
	
}
