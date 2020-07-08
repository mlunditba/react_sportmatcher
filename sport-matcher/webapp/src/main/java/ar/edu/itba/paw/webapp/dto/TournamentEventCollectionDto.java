package ar.edu.itba.paw.webapp.dto;

import java.util.List;

public class TournamentEventCollectionDto {
	
	private List<TournamentEventDto> events;
	private int round;
	private int currentRound;
	
	public static TournamentEventCollectionDto ofEvents(List<TournamentEventDto> events, int round, int currentRound) {
		TournamentEventCollectionDto dto = new TournamentEventCollectionDto();
		dto.events = events;
		dto.round = round;
		dto.currentRound = currentRound;
		
		return dto;
	}

	public List<TournamentEventDto> getEvents() {
		return events;
	}

	public void setEvents(List<TournamentEventDto> events) {
		this.events = events;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public int getCurrentRound() {
		return currentRound;
	}

	public void setCurrentRound(int currentRound) {
		this.currentRound = currentRound;
	}

}
