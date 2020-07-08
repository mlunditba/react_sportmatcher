package ar.edu.itba.paw.webapp.dto.form;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

public class ClubForm {
	
	@NotBlank
	@Pattern(regexp = "[a-zA-Z0-9 ]+")
	@Size(max=100)
	private String name;

	@NotBlank
	@Size(max=500)
	private String location;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() { return location; }

	public void setLocation(String location) {
		this.location = location;
	}
	
}
