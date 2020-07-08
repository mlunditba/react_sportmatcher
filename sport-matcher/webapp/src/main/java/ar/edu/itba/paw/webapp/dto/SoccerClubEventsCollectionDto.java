package ar.edu.itba.paw.webapp.dto;

import java.util.List;

public class SoccerClubEventsCollectionDto {
	
	private List<EventDto> events;
	private int pitchCount;
	
	public static SoccerClubEventsCollectionDto ofEvents(List<EventDto> events, int pitchCount) {
		SoccerClubEventsCollectionDto dto = new SoccerClubEventsCollectionDto();
		dto.events = events;
		dto.pitchCount = pitchCount;
		
		return dto;
	}

	public List<EventDto> getEvents() {
		return events;
	}

	public void setEvents(List<EventDto> events) {
		this.events = events;
	}

	public int getPitchCount() {
		return pitchCount;
	}

	public void setPitchCount(int pitchCount) {
		this.pitchCount = pitchCount;
	}

}
