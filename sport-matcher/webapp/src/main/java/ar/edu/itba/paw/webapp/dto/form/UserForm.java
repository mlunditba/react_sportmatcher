package ar.edu.itba.paw.webapp.dto.form;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

public class UserForm {

	@NotBlank
	@Pattern(regexp = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$")
	private String username;

	@NotBlank
	@Size(min = 6, max = 100)
	private String password;

	@NotBlank
	@Pattern(regexp = "[a-zA-Z]+")
	@Size(max = 100)
	private String firstname;

	@NotBlank
	@Pattern(regexp = "[a-zA-Z]+")
	@Size(max = 100)
	private String lastname;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstname() { return firstname; }

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() { return lastname; }

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

}
