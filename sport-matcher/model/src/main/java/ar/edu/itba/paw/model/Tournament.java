package ar.edu.itba.paw.model;

import java.time.Instant;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
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
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "tournaments")
public class Tournament {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tournaments_tournamentid_seq")
	@SequenceGenerator(sequenceName = "tournaments_tournamentid_seq", name = "tournaments_tournamentid_seq", allocationSize = 1)
	@Column(name = "tournamentid")
	private long tournamentid;
	
	@Column(name = "tournamentname", length = 100, nullable = false)
	private String name;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "sport", length = 50, nullable = false)
	private Sport sport;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "clubid")
	private Club tournamentClub;
	
	@Column(name = "max_teams", nullable = false)
	private int maxTeams;
	
	@Column(name = "team_size", nullable = false)
	private int teamSize;
	
	@Convert(converter = InstantTimestampConverter.class)
	@Column(name = "inscription_ends_at", nullable = false)
	private Instant endsInscriptionAt;
	
	@Column(name = "inscription_success", nullable = false)
	private boolean inscriptionSuccess;
	
	@Convert(converter = InstantTimestampConverter.class)
	@Column(name = "tournament_created_at", nullable = false)
	private Instant createdAt;
	
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "tournament")
	private Set<TournamentTeam> teams;
	
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "tournament")
	private Set<TournamentEvent> tournamentEvents;
	
	@PrePersist
	protected void onCreate() {
		createdAt = Instant.now();
	}
	
	/*package*/ Tournament() {
		
	}
	
	public Tournament(String name, Sport sport, Club tournamentClub,
			int maxTeams, int teamSize, Instant endsInscriptionAt) {
		this.name = name;
		this.sport = sport;
		this.tournamentClub = tournamentClub;
		this.maxTeams = maxTeams;
		this.teamSize = teamSize;
		this.endsInscriptionAt = endsInscriptionAt;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(!(o instanceof Tournament))
			return false;
		Tournament other = (Tournament) o;
		return this.getTournamentid() == other.getTournamentid();
	}
	
	@Override
	public int hashCode() {
		return ((Long) this.getTournamentid()).hashCode();
	}

	public long getTournamentid() {
		return tournamentid;
	}

	public String getName() {
		return name;
	}

	public Sport getSport() {
		return sport;
	}

	public Club getTournamentClub() {
		return tournamentClub;
	}

	public int getMaxTeams() {
		return maxTeams;
	}

	public int getTeamSize() {
		return teamSize;
	}

	public Instant getEndsInscriptionAt() {
		return endsInscriptionAt;
	}
	
	public boolean getInscriptionSuccess() {
		return inscriptionSuccess;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public Set<TournamentTeam> getTeams() {
		return teams;
	}

	public Set<TournamentEvent> getTournamentEvents() {
		return tournamentEvents;
	}
	
	public int getRounds() {
		return maxTeams - 1;
	}

}
