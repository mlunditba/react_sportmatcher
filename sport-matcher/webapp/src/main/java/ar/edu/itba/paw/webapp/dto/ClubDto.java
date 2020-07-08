package ar.edu.itba.paw.webapp.dto;

import java.time.Instant;

import ar.edu.itba.paw.model.Club;

public class ClubDto {
	
	private long clubid;
	private String name;
	private String location;
	private Instant createdAt;
	
	public static ClubDto ofClub(Club club) {
		if(club == null)
			return null;
		
		ClubDto dto = new ClubDto();
		dto.clubid = club.getClubid();
		dto.name = club.getName();
		dto.location = club.getLocation();
		dto.createdAt = club.getCreatedAt();
		
		return dto;
	}

	public long getClubid() {
		return clubid;
	}

	public void setClubid(long clubid) {
		this.clubid = clubid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

}
