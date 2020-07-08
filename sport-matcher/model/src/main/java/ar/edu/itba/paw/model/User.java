package ar.edu.itba.paw.model;

import java.time.Instant;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_userid_seq")
	@SequenceGenerator(sequenceName = "users_userid_seq", name = "users_userid_seq", allocationSize = 1)
	@Column(name = "userid")
	private long userid;
	
	@Column(length = 100, nullable = false, unique = true)
	private String username;
	
	@Column(length = 100, nullable = false)
	private String firstname;
	
	@Column(length = 100, nullable = false)
	private String lastname;
	
	@Column(length = 100, nullable = false)
	private String password;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 50, nullable = false)
	private Role role;
	
	@Column(name = "created_at", nullable = false)
	private Instant createdAt;
	
	@PrePersist
	protected void onCreate() {
		createdAt = Instant.now();
	}
	
	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = false, mappedBy = "owner")
	private List<Event> ownedEvents;
	
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "inscriptedUser")
	private List<Inscription> inscriptions;
	
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "receiver")
	private List<UserComment> comments;
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "addedBy", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private ProfilePicture profilePicture;
	
	/*package*/ User() {
		
	}
	
	public User(String username, String firstname, String lastname, 
			String password, Role role, Instant createdAt) {
		super();
		this.username = username;
		this.firstname = firstname;
		this.lastname = lastname;
		this.password = password;
		this.role = role;
		this.createdAt = createdAt;
	}
	
	public User(long userid, String username, String firstname, String lastname, 
			String password, Role role, Instant createdAt) {
		this(username, firstname, lastname, password, role, createdAt);
		this.userid = userid;
	}
	
	@Override
	public String toString() {
		return "Userid: " + userid + " Username: " + username;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(!(o instanceof User))
			return false;
		User other = (User) o;
		return this.getUsername().equals(other.getUsername());
	}
	
	@Override
	public int hashCode() {
		return this.getUsername().hashCode();
	}

	public long getUserid() {
		return userid;
	}

	public String getUsername() {
		return username;
	}
	
	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public String getPassword() {
		return password;
	}
	
	public Role getRole() {
		return role;
	}
	
	public Instant getCreatedAt() {
		return createdAt;
	}
	
	public List<Event> getOwnedEvents() {
		return ownedEvents;
	}
	
	public List<Inscription> getInscriptions() {
		return inscriptions;
	}
	
	public List<UserComment> getComments() {
		return comments;
	}
	
	public ProfilePicture getProfilePicture() {
		return profilePicture;
	}

}
