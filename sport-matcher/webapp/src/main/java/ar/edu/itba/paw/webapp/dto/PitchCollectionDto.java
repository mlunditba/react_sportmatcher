package ar.edu.itba.paw.webapp.dto;

import java.util.List;

public class PitchCollectionDto {
	
	private List<PitchDto> pitches;
	private int pitchCount;
	private int pageCount;
	private int initialPageIndex;
	
	public static PitchCollectionDto ofPitches(List<PitchDto> pitches, int pitchCount, int pageCount) {
		PitchCollectionDto dto = new PitchCollectionDto();
		dto.pitches = pitches;
		dto.pitchCount = pitchCount;
		dto.pageCount = pageCount;
		
		return dto;
	}
	
	public static PitchCollectionDto ofPitches(List<PitchDto> pitches, int pitchCount, int pageCount, int initialPageIndex) {
		PitchCollectionDto dto = new PitchCollectionDto();
		dto.pitches = pitches;
		dto.pitchCount = pitchCount;
		dto.pageCount = pageCount;
		dto.initialPageIndex = initialPageIndex;
		
		return dto;
	}

	public List<PitchDto> getPitches() {
		return pitches;
	}

	public void setPitches(List<PitchDto> pitches) {
		this.pitches = pitches;
	}

	public int getPitchCount() {
		return pitchCount;
	}

	public void setPitchCount(int pitchCount) {
		this.pitchCount = pitchCount;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getInitialPageIndex() {
		return initialPageIndex;
	}

	public void setInitialPageIndex(int initialPageIndex) {
		this.initialPageIndex = initialPageIndex;
	}

}
