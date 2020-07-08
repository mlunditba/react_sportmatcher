package ar.edu.itba.paw.interfaces;

import java.util.List;
import java.util.Optional;

import ar.edu.itba.paw.exception.UserAlreadyExistsException;
import ar.edu.itba.paw.model.Role;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserComment;

public interface UserDao {
	
	public Optional<User> findById(final long userid);
	
	public Optional<User> findByUsername(final String username);
	
	public Optional<Integer> countVotesReceived(final long userid);
	
	public UserComment createComment(final User commenter, final User receiver, final String comment);
	
	public Optional<UserComment> getComment(final long id);
	
	public List<UserComment> getCommentsByUser(final long userid, final int pageNum);
	
	public User create(String username, String firstname, String lastname, 
			String password, Role role) throws UserAlreadyExistsException;
	
	public int countByUserComments(final long userid);
	
	public int getCommentsPageInitIndex(final int pageNum);

	public int getCommentsMaxPage(final long userid);

}
