package ar.edu.itba.paw.model;

import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "tournament_teams")
public class TournamentTeam {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tournaments_teamid_seq")
	@SequenceGenerator(sequenceName = "tournaments_teamid_seq", name = "tournaments_teamid_seq", allocationSize = 1)
	@Column(name = "teamid")
	private long teamid;
	
	@Column(name = "teamname", length = 100, nullable = false)
	private String teamName;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "tournamentid")
	private Tournament tournament;
	
	@Column(name = "teamscore", nullable = false)
	private int teamScore;
	
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "tournamentTeam")
	private List<Inscription> inscriptions;
	
	/*package*/ TournamentTeam() {
		
	}
	
	public TournamentTeam(String teamName, Tournament tournament) {
		this.teamName = teamName;
		this.tournament = tournament;
		this.teamScore = 0;
	}
	
	@Override
	public String toString() {
		return "Team Id = " + teamid;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(!(o instanceof TournamentTeam))
			return false;
		TournamentTeam other = (TournamentTeam) o;
		return this.getTeamid() == other.getTeamid();
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.getTeamid());
	}
	
	public long getTeamid() {
		return teamid;
	}
	
	public String getTeamName() {
		return teamName;
	}
	
	public Tournament getTournament() {
		return tournament;
	}

	public int getTeamScore() {
		return teamScore;
	}

	public List<Inscription> getInscriptions() {
		return inscriptions;
	}
	
}
