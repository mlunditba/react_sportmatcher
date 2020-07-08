package ar.edu.itba.paw.webapp.dto;

import java.time.Instant;

import ar.edu.itba.paw.model.ClubComment;

public class ClubCommentDto {
	
	private long commentId;
	private String comment;
	private UserDto commenter;
	private ClubDto club;
	private Instant createdAt;
	
	public static ClubCommentDto ofComment(ClubComment comment) {
		if(comment == null)
			return null;
		
		ClubCommentDto dto = new ClubCommentDto();
		dto.commentId = comment.getCommentId();
		dto.comment = comment.getComment();
		dto.commenter = UserDto.ofUser(comment.getCommenter());
		dto.club = ClubDto.ofClub(comment.getClub());
		dto.createdAt = comment.getCreatedAt();

		return dto;
	}

	public long getCommentId() {
		return commentId;
	}

	public void setCommentId(long commentId) {
		this.commentId = commentId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public UserDto getCommenter() {
		return commenter;
	}

	public void setCommenter(UserDto commenter) {
		this.commenter = commenter;
	}

	public ClubDto getClub() {
		return club;
	}

	public void setClub(ClubDto club) {
		this.club = club;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

}
