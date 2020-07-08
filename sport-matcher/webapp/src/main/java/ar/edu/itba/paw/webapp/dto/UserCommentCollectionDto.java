package ar.edu.itba.paw.webapp.dto;

import java.util.List;

public class UserCommentCollectionDto {
	
	private List<UserCommentDto> comments;
	private int commentCount;
	private int pageCount;
	private int commentsPageInitIndex;
	
	public static UserCommentCollectionDto ofComments(List<UserCommentDto> comments, int commentCount,
			int pageCount, int commentsPageInitIndex) {
		UserCommentCollectionDto dto = new UserCommentCollectionDto();
		dto.comments = comments;
		dto.commentCount = commentCount;
		dto.pageCount = pageCount;
		dto.commentsPageInitIndex = commentsPageInitIndex;
		
		return dto;
	}

	public List<UserCommentDto> getComments() {
		return comments;
	}

	public void setComments(List<UserCommentDto> comments) {
		this.comments = comments;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getCommentsPageInitIndex() {
		return commentsPageInitIndex;
	}

	public void setCommentsPageInitIndex(int commentsPageInitIndex) {
		this.commentsPageInitIndex = commentsPageInitIndex;
	}

}
