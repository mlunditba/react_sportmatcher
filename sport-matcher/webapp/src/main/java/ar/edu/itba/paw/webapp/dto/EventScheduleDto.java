package ar.edu.itba.paw.webapp.dto;

import java.util.List;

public class EventScheduleDto {
	
	private List<EventCollectionDto> schedule;
	
	public static EventScheduleDto ofEvents(List<EventCollectionDto> schedule) {
		EventScheduleDto dto = new EventScheduleDto();
		dto.schedule = schedule;
		
		return dto;
	}

	public List<EventCollectionDto> getSchedule() {
		return schedule;
	}

	public void setSchedule(List<EventCollectionDto> schedule) {
		this.schedule = schedule;
	}

}
