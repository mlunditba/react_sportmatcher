package ar.edu.itba.paw.webapp.dto.form;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import ar.edu.itba.paw.webapp.dto.form.validator.FutureDate;
import ar.edu.itba.paw.webapp.dto.form.validator.IntegerRange;

public class EventForm {
	
	@NotBlank
	@Size(max = 100)
	private String name;
	
	@Size(max = 500)
	private String description;

	@IntegerRange(min = 1, max = 100)
	private Integer maxParticipants;
	
	@FutureDate(startOfDay = true)
	private String date;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getMaxParticipants() {
		return maxParticipants;
	}

	public void setMaxParticipants(Integer maxParticipants) {
		this.maxParticipants = maxParticipants;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
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
