package ar.edu.itba.paw.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
@Table(name = "events_users")
public class Inscription {
	
	@EmbeddedId
	private InscriptionId id;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@MapsId("eventid")
	@JoinColumn(name = "eventid", nullable = false)
	private Event inscriptionEvent;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@MapsId("userid")
	@JoinColumn(name = "userid", nullable = false)
	private User inscriptedUser;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "teamid", nullable = true)
	private TournamentTeam tournamentTeam;
	
	@Column(name = "vote", nullable = true)
	private Integer vote;
	
	/*package*/ Inscription() {
		
	}
	
	public Inscription(Event inscriptionEvent, User inscriptedUser, Integer vote) {
		this.inscriptionEvent = inscriptionEvent;
		this.inscriptedUser = inscriptedUser;
		this.id = new InscriptionId(inscriptionEvent.getEventId(), inscriptedUser.getUserid());
		this.vote = vote;
	}
	
	public Inscription(Event event, User inscriptedUser) {
		this.inscriptionEvent = event;
		this.inscriptedUser = inscriptedUser;
		this.id = new InscriptionId(event.getEventId(), inscriptedUser.getUserid());
	}
	
	public Inscription(Event event, User inscriptedUser, TournamentTeam team) {
		this.inscriptionEvent = event;
		this.inscriptedUser = inscriptedUser;
		this.tournamentTeam = team;
		this.id = new InscriptionId(event.getEventId(), inscriptedUser.getUserid());
	}
	
	@Override
	public String toString() {
		return "Inscription: Event = " + inscriptionEvent + " User = " + inscriptedUser;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(!(o instanceof Inscription))
			return false;
		Inscription other = (Inscription) o;
		return this.id.equals(other.id);
	}
	
	@Override
	public int hashCode() {
		return this.inscriptedUser.hashCode();
	}

	public Event getInscriptionEvent() {
		return inscriptionEvent;
	}

	public User getInscriptedUser() {
		return inscriptedUser;
	}
	
	public TournamentTeam getTournamentTeam() {
		return tournamentTeam;
	}

	public Integer getVote() {
		return vote;
	}
	
}
