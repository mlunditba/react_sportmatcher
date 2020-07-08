package ar.edu.itba.paw.model;

import java.time.Instant;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "events")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "events_eventid_seq")
	@SequenceGenerator(sequenceName = "events_eventid_seq", name = "events_eventid_seq", allocationSize = 1)
	@Column(name = "eventid")
	private long eventid;
	
	@Column(name = "eventname", length = 100, nullable = false)
	private String name;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "userid")
	private User owner;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "pitchid")
	private Pitch pitch;
	
	@Column(length = 500, nullable = true)
	private String description;
	
	@Column(name = "max_participants", nullable = false)
	private int maxParticipants;
	
	@Convert(converter = InstantTimestampConverter.class)
	@Column(name = "inscription_ends_at", nullable = true)
	private Instant endsInscriptionAt;
	
	@Column(name = "inscription_success", nullable = true)
	private Boolean inscriptionSuccess;
	
	@Column(name = "starts_at", nullable = false)
	private Instant startsAt;

	@Column(name = "ends_at", nullable = false)
	private Instant endsAt;
	
	@Column(name = "event_created_at", nullable = false)
	private Instant createdAt;
	
	@PrePersist
	protected void onCreate() {
		createdAt = Instant.now();
		inscriptionSuccess = false;
	}
	
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "inscriptionEvent")
	private List<Inscription> inscriptions;
	
	/*package*/ Event() {
		
	}
	
	public Event(String name, Pitch pitch, String description,
			int maxParticipants, Instant startsAt, Instant endsAt, Instant inscriptionEndDate) {
		super();
		this.name = name;
		this.pitch = pitch;
		this.description = description;
		this.maxParticipants = maxParticipants;
		this.startsAt = startsAt;
		this.endsAt = endsAt;
		this.endsInscriptionAt = inscriptionEndDate;
	}
	
	public Event(String name, User owner, Pitch pitch, String description,
	int maxParticipants, Instant startsAt, Instant endsAt, Instant inscriptionEndDate) {
		this(name, pitch, description,
			maxParticipants, startsAt, endsAt, inscriptionEndDate);
		this.owner = owner;
	}
	
	public Event(String name, User owner, Pitch pitch, int maxParticipants, Instant startsAt, 
			Instant endsAt) {
		this.name = name;
		this.owner = owner;
		this.pitch = pitch;
		this.maxParticipants = maxParticipants;
		this.startsAt = startsAt;
		this.endsAt = endsAt;
	}
	
	@Override
	public String toString() {
		return "Eventid: " + eventid + " Name: " + name;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(!(o instanceof Event))
			return false;
		Event other = (Event) o;
		return this.eventid == other.eventid;
	}
	
	@Override
	public int hashCode() {
		return ((Long)this.eventid).hashCode();
	}
	
	public long getEventId() {
		return eventid;
	}

	public String getName() {
		return name;
	}
	
	public User getOwner() {
		return owner;
	}
	
	public Pitch getPitch() {
		return pitch;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public int getMaxParticipants() {
		return maxParticipants;
	}

	public Instant getEndsInscriptionAt() {
		return endsInscriptionAt;
	}
	
	public Boolean getInscriptionSuccess() {
		return inscriptionSuccess;
	}

	public Instant getStartsAt() {
		return startsAt;
	}

	public Instant getEndsAt() {
		return endsAt;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}
	
	public List<Inscription> getInscriptions() {
		return inscriptions;
	}

}
