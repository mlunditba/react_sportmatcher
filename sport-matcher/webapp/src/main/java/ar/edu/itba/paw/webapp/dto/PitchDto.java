package ar.edu.itba.paw.webapp.dto;

import java.time.Instant;

import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.Sport;

public class PitchDto {
	
	private long pitchid;
	private ClubDto club;
	private String name;
	private Sport sport;
	private Instant createdAt;
	
	public static PitchDto ofPitch(Pitch pitch) {
		if(pitch == null)
			return null;

		PitchDto dto = new PitchDto();
		dto.pitchid = pitch.getPitchid();
		dto.club = ClubDto.ofClub(pitch.getClub());
		dto.name = pitch.getName();
		dto.sport = pitch.getSport();
		dto.createdAt = pitch.getCreatedAt();
		
		return dto;
	}

	public long getPitchid() {
		return pitchid;
	}

	public void setPitchid(long pitchid) {
		this.pitchid = pitchid;
	}

	public ClubDto getClub() {
		return club;
	}

	public void setClub(ClubDto club) {
		this.club = club;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Sport getSport() {
		return sport;
	}

	public void setSport(Sport sport) {
		this.sport = sport;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

}
