package ar.edu.itba.paw.model;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "club_comments")
public class ClubComment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "club_comments_commentid_seq")
	@SequenceGenerator(sequenceName = "club_comments_commentid_seq", name = "club_comments_commentid_seq", allocationSize = 1)
	@Column(name = "commentid")
	private long commentid;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "commenter_id", nullable = false)
	private User commenter;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "dest_clubid", nullable = false)
	private Club club;
	
	@Column(name = "comment", nullable = false)
	private String comment;
	
	@Column(name = "created_at", nullable = false)
	private Instant createdAt;
	
	@PrePersist
	protected void onCreate() {
		createdAt = Instant.now();
	}
	
	/*package*/ ClubComment() {
		
	}
	
	public ClubComment(User commenter, Club club, String comment) {
		super();
		this.commenter = commenter;
		this.club = club;
		this.comment = comment;
	}
	
	@Override
	public String toString() {
		return "Club Comment: Commenter = " + commenter + " Club = " + club
				+ " Created at = " + createdAt;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(!(o instanceof ClubComment))
			return false;
		ClubComment other = (ClubComment) o;
		return this.commentid == other.commentid;
	}
	
	@Override
	public int hashCode() {
		return ((Long)this.commentid).hashCode();
	}
	
	public long getCommentId() {
		return commentid;
	}

	public User getCommenter() {
		return commenter;
	}

	public Club getClub() {
		return club;
	}

	public String getComment() {
		return comment;
	}
	
	public Instant getCreatedAt() {
		return createdAt;
	}

}
