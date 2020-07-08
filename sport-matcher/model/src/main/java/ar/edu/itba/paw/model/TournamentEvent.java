package ar.edu.itba.paw.model;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tournament_events")
public class TournamentEvent extends Event {
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "tournamentid")
	private Tournament tournament;
	
	@Column(name = "round", nullable = false)
	private int round;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "first_teamid")
	private TournamentTeam firstTeam;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "second_teamid")
	private TournamentTeam secondTeam;
	
	@Column(name = "first_team_score", nullable = true)
	private Integer firstTeamScore;
	
	@Column(name = "second_team_score", nullable = true)
	private Integer secondTeamScore;
	
	/*package*/ TournamentEvent() {
		
	}
	
	public TournamentEvent(String name, User owner, Pitch pitch,
			int maxParticipants, Instant startsAt, Instant endsAt, Tournament tournament, int round,
			TournamentTeam firstTeam, TournamentTeam secondTeam) {
		super(name, owner, pitch, maxParticipants, startsAt, endsAt);
		this.tournament = tournament;
		this.round = round;
		this.firstTeam = firstTeam;
		this.secondTeam = secondTeam;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(!(o instanceof TournamentEvent))
			return false;
		TournamentEvent other = (TournamentEvent) o;
		return this.getEventId() == other.getEventId();
	}

	public Tournament getTournament() {
		return tournament;
	}

	public int getRound() {
		return round;
	}

	public TournamentTeam getFirstTeam() {
		return firstTeam;
	}

	public TournamentTeam getSecondTeam() {
		return secondTeam;
	}

	public Integer getFirstTeamScore() {
		return firstTeamScore;
	}

	public Integer getSecondTeamScore() {
		return secondTeamScore;
	}
	
}
