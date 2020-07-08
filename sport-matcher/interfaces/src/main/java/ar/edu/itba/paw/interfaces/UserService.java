package ar.edu.itba.paw.interfaces;

import java.util.List;
import java.util.Optional;

import ar.edu.itba.paw.exception.EntityNotFoundException;
import ar.edu.itba.paw.exception.PictureProcessingException;
import ar.edu.itba.paw.exception.UserAlreadyExistsException;
import ar.edu.itba.paw.exception.UserNotAuthorizedException;
import ar.edu.itba.paw.model.Role;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserComment;

public interface UserService {
	
	public Optional<User> findById(final long userid);
	
	public Optional<User> findByUsername(final String username);
	
	public int countVotesReceived(final long userid);
	
	public UserComment createComment(final long commenterid, final long receiverid, final String comment)
			throws UserNotAuthorizedException, EntityNotFoundException;
	
	public Optional<UserComment> getComment(final long id);
	
	/**
	 * Returns true if the commenter and receiver have an Inscription for a common past Event
	 * or if the commenter has an Inscription for a past Event which was owned by the receiver.
	 * @param commenterid	The commenter's id.
	 * @param receiverid	The receiver's id.
	 * @return true if the commenter and receiver have an Inscription for a common past Event
	 * or if the commenter has an Inscription for a past Event which was owned by the receiver.
	 * @throws EntityNotFoundException if the commenter or receiver do not exist
	 */
	public boolean haveRelationship(final long commenterid, final long receiverid) throws EntityNotFoundException;
	
	/**
	 * 
	 * @param username
	 * @param firstname
	 * @param lastname
	 * @param password
	 * @param role
	 * @param picture
	 * @return
	 * @throws UserAlreadyExistsException If a user with that username existed already
	 */
	public User create(String username, String firstname, String lastname, 
			String password, Role role, byte[] picture)
			throws UserAlreadyExistsException, PictureProcessingException;

	public List<UserComment> getCommentsByUser(final long userid, final int pageNum);
	
	public int countByUserComments(final long userid);
	
	public int getCommentsPageInitIndex(final int pageNum);

	public int getCommentsMaxPage(final long userid);

}
