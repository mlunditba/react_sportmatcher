package ar.edu.itba.paw.webapp.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ar.edu.itba.paw.model.Inscription;

@XmlRootElement
public class InscriptionDto {

	private UserDto inscriptedUser;
	@XmlElement(name = "vote", nillable = true, required = true)
	private Integer vote;
	
	public static InscriptionDto ofInscription(Inscription inscription) {
		InscriptionDto dto = new InscriptionDto();
		dto.inscriptedUser = UserDto.ofUser(inscription.getInscriptedUser());
		dto.vote = inscription.getVote();
		return dto;
	}
	
	public UserDto getInscriptedUser() {
		return inscriptedUser;
	}
	public void setInscriptedUser(UserDto inscriptedUser) {
		this.inscriptedUser = inscriptedUser;
	}
	public Integer getVote() {
		return vote;
	}
	public void setVote(Integer vote) {
		this.vote = vote;
	}
	
}
