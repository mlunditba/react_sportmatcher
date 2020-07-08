package ar.edu.itba.paw.webapp.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ar.edu.itba.paw.model.Role;
import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.model.User;

@XmlRootElement
public class FullUserDto {
	
	private long userid;
	private String username;
	private String firstname;
	private String lastname;
	private String role;
	private int currentEventCount;
	private int currentEventsOwned;
	private int pastEventsParticipant;

	@XmlElement(name = "favoriteClub", nillable = true, required = true)
	private ClubDto favoriteClub;

	private Sport favoriteSport;

	private int votesReceived;
	
	public static FullUserDto ofUser(User user, int currentEventCount, Sport favoriteSport,
			int currEventsOwned, int pastEventsParticipant, ClubDto favoriteClub, int votesReceived) {
		FullUserDto dto = new FullUserDto();
		dto.userid = user.getUserid();
		dto.username = user.getUsername();
		dto.firstname = user.getFirstname();
		dto.lastname = user.getLastname();
		dto.role = user.getRole().equals(Role.ROLE_ADMIN) ? "admin" : "user";
		dto.currentEventCount = currentEventCount;
		dto.favoriteSport = favoriteSport;
		dto.currentEventsOwned = currEventsOwned;
		dto.pastEventsParticipant = pastEventsParticipant;
		dto.favoriteClub = favoriteClub;
		dto.votesReceived = votesReceived; 

		return dto;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public int getCurrentEventCount() {
		return currentEventCount;
	}

	public void setCurrentEventCount(int currentEventCount) {
		this.currentEventCount = currentEventCount;
	}

	public Sport getFavoriteSport() {
		return favoriteSport;
	}

	public void setFavoriteSport(Sport favoriteSport) {
		this.favoriteSport = favoriteSport;
	}

	public int getCurrentEventsOwned() {
		return currentEventsOwned;
	}

	public void setCurrentEventsOwned(int currentEventsOwned) {
		this.currentEventsOwned = currentEventsOwned;
	}

	public int getPastEventsParticipant() {
		return pastEventsParticipant;
	}

	public void setPastEventsParticipant(int pastEventsParticipant) {
		this.pastEventsParticipant = pastEventsParticipant;
	}

	public ClubDto getFavoriteClub() {
		return favoriteClub;
	}

	public void setFavoriteClub(ClubDto favoriteClub) {
		this.favoriteClub = favoriteClub;
	}

	public int getVotesReceived() {
		return votesReceived;
	}

	public void setVotesReceived(int votesReceived) {
		this.votesReceived = votesReceived;
	}

}
