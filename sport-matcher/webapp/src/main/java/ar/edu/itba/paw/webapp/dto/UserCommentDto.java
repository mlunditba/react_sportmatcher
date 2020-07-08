package ar.edu.itba.paw.webapp.dto;

import java.time.Instant;

import ar.edu.itba.paw.model.UserComment;

public class UserCommentDto {
	
	private long commentId;
	private String comment;
	private UserDto commenter;
	private UserDto receiver;
	private Instant createdAt;
	
	public static UserCommentDto ofComment(UserComment comment) {
		if(comment == null)
			return null;
		
		UserCommentDto dto = new UserCommentDto();
		dto.commentId = comment.getCommentId();
		dto.comment = comment.getComment();
		dto.commenter = UserDto.ofUser(comment.getCommenter());
		dto.receiver = UserDto.ofUser(comment.getReceiver());
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

	public UserDto getReceiver() {
		return receiver;
	}

	public void setReceiver(UserDto receiver) {
		this.receiver = receiver;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

}
