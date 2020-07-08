package ar.edu.itba.paw.model;

import java.time.Instant;
import java.util.Objects;

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
@Table(name = "user_comments")
public class UserComment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_comments_commentid_seq")
	@SequenceGenerator(sequenceName = "user_comments_commentid_seq", name = "user_comments_commentid_seq", allocationSize = 1)
	@Column(name = "commentid")
	private long commentid;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "commenter_id", nullable = false)
	private User commenter;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "dest_userid", nullable = false)
	private User receiver;
	
	@Column(name = "comment", nullable = false)
	private String comment;
	
	@Column(name = "created_at", nullable = false)
	private Instant createdAt;
	
	@PrePersist
	protected void onCreate() {
		createdAt = Instant.now();
	}
	
	/*package*/ UserComment() {
		
	}
	
	public UserComment(User commenter, User receiver, String comment) {
		super();
		this.commenter = commenter;
		this.receiver = receiver;
		this.comment = comment;
	}
	
	@Override
	public String toString() {
		return "User Comment: Commenter = " + commenter + " Receiver = " + receiver 
				+ " Created at = " + createdAt;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(!(o instanceof UserComment))
			return false;
		UserComment other = (UserComment) o;
		return this.commentid == other.commentid;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.commentid);
	}
	
	public long getCommentId() {
		return commentid;
	}

	public User getCommenter() {
		return commenter;
	}

	public User getReceiver() {
		return receiver;
	}

	public String getComment() {
		return comment;
	}
	
	public Instant getCreatedAt() {
		return createdAt;
	}

}
