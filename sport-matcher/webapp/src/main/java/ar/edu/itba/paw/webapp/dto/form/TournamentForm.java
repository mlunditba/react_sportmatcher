package ar.edu.itba.paw.webapp.dto.form;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import ar.edu.itba.paw.webapp.dto.form.validator.FutureDate;
import ar.edu.itba.paw.webapp.dto.form.validator.IntegerRange;

public class TournamentForm {
	
	@NotBlank
	@Pattern(regexp = "[a-zA-Z0-9 ]+")
	@Size(max=100)
	private String name;
	
	@IntegerRange(min = 4, max = 10)
	private Integer maxTeams;

	@IntegerRange(min = 3, max = 11)
	private Integer teamSize;
	
	@FutureDate(startOfDay = true)
	private String firstRoundDate;
	
	@IntegerRange(min = 0, max = 23)
	private Integer startsAtHour;
	
	@IntegerRange(min = 0, max = 23)
	private Integer endsAtHour;
	
	@FutureDate
	private String inscriptionEndDate;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getMaxTeams() {
		return maxTeams;
	}

	public void setMaxTeams(Integer maxTeams) {
		this.maxTeams = maxTeams;
	}

	public Integer getTeamSize() {
		return teamSize;
	}

	public void setTeamSize(Integer teamSize) {
		this.teamSize = teamSize;
	}

	public String getFirstRoundDate() {
		return firstRoundDate;
	}

	public void setFirstRoundDate(String firstRoundDate) {
		this.firstRoundDate = firstRoundDate;
	}

	public Integer getStartsAtHour() {
		return startsAtHour;
	}

	public void setStartsAtHour(Integer startsAtHour) {
		this.startsAtHour = startsAtHour;
	}

	public Integer getEndsAtHour() {
		return endsAtHour;
	}

	public void setEndsAtHour(Integer endsAtHour) {
		this.endsAtHour = endsAtHour;
	}

	public String getInscriptionEndDate() {
		return inscriptionEndDate;
	}

	public void setInscriptionEndDate(String inscriptionEndDate) {
		this.inscriptionEndDate = inscriptionEndDate;
	}

}
