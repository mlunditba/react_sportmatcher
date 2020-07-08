package ar.edu.itba.paw.webapp.dto;

import java.time.Instant;

import ar.edu.itba.paw.model.TournamentEvent;

public class TournamentEventDto {
	
	private long eventid;
	private String name;
	private int maxParticipants;
	private PitchDto pitch;
	private TournamentTeamDto firstTeam;
	private TournamentTeamDto secondTeam;
	private Integer firstTeamScore;
	private Integer secondTeamScore;
	private Instant startsAt;
	private Instant endsAt;
	
	public static TournamentEventDto ofTournamentEvent(TournamentEvent event) {
		TournamentEventDto dto = new TournamentEventDto();
		dto.eventid = event.getEventId();
		dto.name = event.getName();
		dto.maxParticipants = event.getMaxParticipants();
		dto.pitch = PitchDto.ofPitch(event.getPitch());
		dto.firstTeam = TournamentTeamDto.ofTeam(event.getFirstTeam());
		dto.secondTeam = TournamentTeamDto.ofTeam(event.getSecondTeam());
		dto.firstTeamScore = event.getFirstTeamScore();
		dto.secondTeamScore = event.getSecondTeamScore();
		dto.startsAt = event.getStartsAt();
		dto.endsAt = event.getEndsAt();

		return dto;
	}

	public long getEventid() {
		return eventid;
	}

	public void setEventid(long eventid) {
		this.eventid = eventid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMaxParticipants() {
		return maxParticipants;
	}

	public void setMaxParticipants(int maxParticipants) {
		this.maxParticipants = maxParticipants;
	}

	public PitchDto getPitch() {
		return pitch;
	}

	public void setPitch(PitchDto pitch) {
		this.pitch = pitch;
	}
	
	public TournamentTeamDto getFirstTeam() {
		return firstTeam;
	}

	public void setFirstTeam(TournamentTeamDto firstTeam) {
		this.firstTeam = firstTeam;
	}

	public TournamentTeamDto getSecondTeam() {
		return secondTeam;
	}

	public void setSecondTeam(TournamentTeamDto secondTeam) {
		this.secondTeam = secondTeam;
	}

	public Integer getFirstTeamScore() {
		return firstTeamScore;
	}

	public void setFirstTeamScore(Integer firstTeamScore) {
		this.firstTeamScore = firstTeamScore;
	}

	public Integer getSecondTeamScore() {
		return secondTeamScore;
	}

	public void setSecondTeamScore(Integer secondTeamScore) {
		this.secondTeamScore = secondTeamScore;
	}

	public Instant getStartsAt() {
		return startsAt;
	}

	public void setStartsAt(Instant startsAt) {
		this.startsAt = startsAt;
	}

	public Instant getEndsAt() {
		return endsAt;
	}

	public void setEndsAt(Instant endsAt) {
		this.endsAt = endsAt;
	}

}
