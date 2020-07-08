package ar.edu.itba.paw.webapp.dto;

import java.util.List;

public class ClubCollectionDto {
	
	private List<ClubDto> clubs;
	private int totalClubsMatching;
	private int pagesCountMatching;
	private int initialPageIndex;
	
	public static ClubCollectionDto ofClubs(List<ClubDto> clubs, int totalClubsMatching,
			int pagesCountMatching, int initialPageIndex) {
		final ClubCollectionDto dto = new ClubCollectionDto();
		dto.clubs = clubs;
		dto.totalClubsMatching = totalClubsMatching;
		dto.pagesCountMatching = pagesCountMatching;
		dto.initialPageIndex = initialPageIndex;
		
		return dto;
	}

	public List<ClubDto> getClubs() {
		return clubs;
	}

	public void setClubs(List<ClubDto> clubs) {
		this.clubs = clubs;
	}

	public int getTotalClubsMatching() {
		return totalClubsMatching;
	}

	public void setTotalClubsMatching(int totalClubsMatching) {
		this.totalClubsMatching = totalClubsMatching;
	}
	
	public int getPagesCountMatching() {
		return pagesCountMatching;
	}

	public void setPagesCountMatching(int pagesCountMatching) {
		this.pagesCountMatching = pagesCountMatching;
	}

	public int getInitialPageIndex() {
		return initialPageIndex;
	}

	public void setInitialPageIndex(int initialPageIndex) {
		this.initialPageIndex = initialPageIndex;
	}

}
