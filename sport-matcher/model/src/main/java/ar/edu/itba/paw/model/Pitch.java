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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "pitches")
public class Pitch {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pitches_pitchid_seq")
	@SequenceGenerator(sequenceName = "pitches_pitchid_seq", name = "pitches_pitchid_seq", allocationSize = 1)
	@Column(name = "pitchid")
	private long pitchid;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "clubid")
	private Club club;
	
	@Column(name = "pitchname", length = 100, nullable = false)
	private String name;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 50, nullable = false)
	private Sport sport;
	
	@Column(name = "pitch_created_at", nullable = false)
	private Instant createdAt;
	
	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "pitch")
	private List<Event> pitchEvents;
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "belongsTo", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private PitchPicture pitchPicture;
	
	/*package*/ Pitch() {
		
	}
	
	public Pitch(Club club, String name, Sport sport, Instant createdAt) {
		super();
		this.club = club;
		this.name = name;
		this.sport = sport;
		this.createdAt = createdAt;
	}
	
	public Pitch(long pitchid, Club club, String name, Sport sport, Instant createdAt) {
		this(pitchid, name, sport, createdAt);
		this.club = club;
	}
	
	public Pitch(long pitchid, String name, Sport sport, Instant createdAt) {
		super();
		this.pitchid = pitchid;
		this.name = name;
		this.sport = sport;
		this.createdAt = createdAt;
	}
	
	@Override
	public String toString() {
		return "Pitch: " + name + "; id: " + pitchid;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(!(o instanceof Pitch))
			return false;
		Pitch other = (Pitch) o;
		return pitchid == other.getPitchid();
	}
	
	@Override
	public int hashCode() {
		return ((Long)this.pitchid).hashCode();
	}

	public long getPitchid() {
		return pitchid;
	}

	public Club getClub() {
		return club;
	}

	public String getName() {
		return name;
	}

	public Sport getSport() {
		return sport;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}
	
	public List<Event> getPitchEvents() {
		return pitchEvents;
	}
	
	public PitchPicture getPitchPicture() {
		return pitchPicture;
	}

}
