package ar.edu.itba.paw.webapp.dto;

import java.util.List;

public class EventCollectionDto {
	
	private List<EventDto> events;
	private int eventCount;
	private int lastPageNum;
	private int initialPageIndex;
	
	public static EventCollectionDto ofEvents(List<EventDto> events, int eventCount, int lastPageNum,
			int initialPageIndex) {
		EventCollectionDto dto = new EventCollectionDto();
		dto.events = events;
		dto.eventCount = eventCount;
		dto.lastPageNum = lastPageNum;
		dto.initialPageIndex = initialPageIndex;
		
		return dto;
	}
	
	public static EventCollectionDto ofEvents(List<EventDto> events) {
		EventCollectionDto dto = new EventCollectionDto();
		dto.events = events;
		
		return dto;
	}

	public List<EventDto> getEvents() {
		return events;
	}

	public void setEvents(List<EventDto> events) {
		this.events = events;
	}

	public int getEventCount() {
		return eventCount;
	}

	public void setEventCount(int eventCount) {
		this.eventCount = eventCount;
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
