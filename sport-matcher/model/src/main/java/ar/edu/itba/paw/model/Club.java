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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "clubs")
public class Club {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "clubs_clubid_seq")
	@SequenceGenerator(sequenceName = "clubs_clubid_seq", name = "clubs_clubid_seq", allocationSize = 1)
	@Column(name = "clubid")
	private long clubid;
	
	@Column(name = "clubname", length = 100, nullable = false)
	private String name;
	
	@Column(name = "location", length = 100, nullable = false)
	private String location;
	
	@Convert(converter = InstantTimestampConverter.class)
	@Column(name = "club_created_at", nullable = false)
	private Instant createdAt;
	
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "club")
	private List<Pitch> clubPitches;
	
	/*package*/ Club() {
		
	}
	
	public Club(String name, String location, Instant createdAt) {
		super();
		this.name = name;
		this.location = location;
		this.createdAt = createdAt;
	}
	
	public Club(long clubid, String name, String location, Instant createdAt) {
		super();
		this.clubid = clubid;
		this.name = name;
		this.location = location;
		this.createdAt = createdAt;
	}
	
	@Override
	public String toString() {
		return "Club: " + name + "; id: " + clubid;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(!(o instanceof Club))
			return false;
		Club other = (Club) o;
		return clubid == other.getClubid();
	}
	
	@Override
	public int hashCode() {
		return ((Long)clubid).hashCode();
	}

	public long getClubid() {
		return clubid;
	}

	public String getName() {
		return name;
	}

	public String getLocation() {
		return location;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}
	
	public List<Pitch> getClubPitches() {
		return clubPitches;
	}

}
