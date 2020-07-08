package ar.edu.itba.paw.webapp.dto;

import java.util.List;

public class ClubCommentCollectionDto {
	
	private List<ClubCommentDto> comments;
	private int commentCount;
	private int pageCount;
	private int commentsPageInitIndex;
	
	public static ClubCommentCollectionDto ofComments(List<ClubCommentDto> comments, int commentCount,
			int pageCount, int commentsPageInitIndex) {
		ClubCommentCollectionDto dto = new ClubCommentCollectionDto();
		dto.comments = comments;
		dto.commentCount = commentCount;
		dto.pageCount = pageCount;
		dto.commentsPageInitIndex = commentsPageInitIndex;
		
		return dto;
	}

	public List<ClubCommentDto> getComments() {
		return comments;
	}

	public void setComments(List<ClubCommentDto> comments) {
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
