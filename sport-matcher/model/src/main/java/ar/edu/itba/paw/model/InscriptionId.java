package ar.edu.itba.paw.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@SuppressWarnings("serial")
@Embeddable
public class InscriptionId implements Serializable {
	
	@Column(name = "eventid", nullable = false)
	private Long eventid;
	
	@Column(name = "userid", nullable = false)
	private Long userid;
	
	/*package*/ InscriptionId() {
		
	}
	
	public InscriptionId(Long eventid, Long userid) {
		this.eventid = eventid;
		this.userid = userid;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(!(o instanceof InscriptionId))
			return false;
		InscriptionId other = (InscriptionId) o;
		return this.getEventid().equals(other.getEventid()) 
				&& this.getUserid().equals(other.getUserid());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.getEventid(), this.getUserid());
	}

	public Long getEventid() {
		return eventid;
	}

	public Long getUserid() {
		return userid;
	}
	
	
}
