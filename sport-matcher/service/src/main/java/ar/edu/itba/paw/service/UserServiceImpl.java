package ar.edu.itba.paw.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.exception.EntityNotFoundException;
import ar.edu.itba.paw.exception.IllegalParamException;
import ar.edu.itba.paw.exception.PictureProcessingException;
import ar.edu.itba.paw.exception.UserAlreadyExistsException;
import ar.edu.itba.paw.exception.UserNotAuthorizedException;
import ar.edu.itba.paw.interfaces.InscriptionDao;
import ar.edu.itba.paw.interfaces.ProfilePictureService;
import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.Role;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserComment;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDao ud;
	
	@Autowired
	private ProfilePictureService pps;
	
	@Autowired
	private InscriptionDao idao;
	
	private static final String NEGATIVE_PAGE_ERROR = "Page number must be greater than zero.";
	private static final String NEGATIVE_ID_ERROR = "Id must be greater than zero.";

	@Override
	public Optional<User> findById(final long userid) {
		if(userid <= 0) {
			throw new IllegalParamException(NEGATIVE_ID_ERROR);
		}
		return ud.findById(userid);
	}

	@Override
	public Optional<User> findByUsername(final String username) {
		return ud.findByUsername(username);
	}
	
	@Override
	public int countVotesReceived(final long userid) {
		if(userid <= 0) {
			throw new IllegalParamException(NEGATIVE_ID_ERROR);
		}
		return ud.countVotesReceived(userid).orElse(0);
	}
	
	@Transactional(rollbackFor = { Exception.class })
	@Override
	public UserComment createComment(final long commenterid, final long receiverid, final String comment) 
			throws UserNotAuthorizedException, EntityNotFoundException {
		if(commenterid <= 0 || receiverid <= 0) {
			throw new IllegalParamException(NEGATIVE_ID_ERROR);
		}
		if(commenterid == receiverid)
			throw new UserNotAuthorizedException("User is not authorized to comment own profile.");
		User commenter = ud.findById(commenterid).orElseThrow(EntityNotFoundException::new);
		User receiver = ud.findById(receiverid).orElseThrow(EntityNotFoundException::new);
		
		if(!idao.haveRelationship(commenter, receiver))
			throw new UserNotAuthorizedException("User is not authorized to comment if no shared events.");
		
		return ud.createComment(commenter, receiver, comment);
	}
	
	@Override
	public Optional<UserComment> getComment(final long id) {
		return ud.getComment(id);
	}
	
	@Override
	public boolean haveRelationship(final long commenterid, final long receiverid) throws EntityNotFoundException {
		if(commenterid <= 0 || receiverid <= 0) {
			throw new IllegalParamException(NEGATIVE_ID_ERROR);
		}
		if(commenterid == receiverid)
			return false;
		
		User commenter = ud.findById(commenterid).orElseThrow(EntityNotFoundException::new);
		User receiver = ud.findById(receiverid).orElseThrow(EntityNotFoundException::new);
		
		return idao.haveRelationship(commenter, receiver);
	}

	@Transactional(rollbackFor = { Exception.class })
	@Override
	public User create(String username, String firstname, String lastname, 
			String password, Role role, byte[] picture)
			throws UserAlreadyExistsException, PictureProcessingException {
		User user = ud.create(username.toLowerCase(), firstname, lastname, password, role);
		if(picture != null) {
			pps.create(user, picture);
		}
		return user;
	}

	@Override
	public List<UserComment> getCommentsByUser(final long userid, final int pageNum) {
		if(userid <= 0) {
			throw new IllegalParamException(NEGATIVE_ID_ERROR);
		}
		if(pageNum <= 0) {
			throw new IllegalParamException(NEGATIVE_PAGE_ERROR);
		}
		return ud.getCommentsByUser(userid, pageNum);
	}

	@Override
	public int countByUserComments(long userid) {
		if(userid <= 0) {
			throw new IllegalParamException(NEGATIVE_ID_ERROR);
		}
		return ud.countByUserComments(userid);
	}
	
	@Override
	public int getCommentsPageInitIndex(final int pageNum) {
		if(pageNum <= 0) {
			throw new IllegalParamException(NEGATIVE_PAGE_ERROR);
		}
		return ud.getCommentsPageInitIndex(pageNum);
	}
	
	@Override
	public int getCommentsMaxPage(final long userid) {
		if(userid <= 0) {
			throw new IllegalParamException(NEGATIVE_ID_ERROR);
		}
		return ud.getCommentsMaxPage(userid);
	}

}
