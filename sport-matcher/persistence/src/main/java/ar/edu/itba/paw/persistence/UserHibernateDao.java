package ar.edu.itba.paw.persistence;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.exception.UserAlreadyExistsException;
import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.model.Role;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserComment;

@Repository
public class UserHibernateDao implements UserDao {
	
	private static final int MAX_ROWS = 10;
	
	@PersistenceContext
	private EntityManager em;

	@Override
	public Optional<User> findById(long userid) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<User> cq = cb.createQuery(User.class);
		Root<User> from = cq.from(User.class);
		
		final TypedQuery<User> query = em.createQuery(
				cq.select(from).where(cb.equal(from.get("userid"), userid))
			);
		
		return query.getResultList().stream().findFirst();
	}

	@Override
	public Optional<User> findByUsername(String username) {
		final TypedQuery<User> query = em.createQuery("FROM User AS u WHERE u.username LIKE :username",
				User.class);
		query.setParameter("username", username);
		query.setMaxResults(1);
		return query.getResultList().stream().findFirst();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Optional<Integer> countVotesReceived(long userid) {
		Query votesQuery = em.createNativeQuery("SELECT sum(vote) FROM events_users "
				+ " WHERE eventid IN (SELECT eventid FROM events WHERE userid = :userid);");
		votesQuery.setParameter("userid", userid);
		List<BigInteger> list = ((List<BigInteger>) votesQuery.getResultList());
		if(list.isEmpty() || list.get(0) == null)
			return Optional.empty();
		return Optional.of(list.get(0).intValue());
	}
	
	@Override
	public UserComment createComment(final User commenter, final User receiver, final String comment) {
		
		final UserComment userComment = new UserComment(commenter, receiver, comment);
		em.persist(userComment);
		
		return userComment;
	}
	
	@Override
	public Optional<UserComment> getComment(final long id) {
		return Optional.ofNullable(em.find(UserComment.class, id));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserComment> getCommentsByUser(final long userid, final int pageNum) {
		String idQueryString = "SELECT commentid FROM user_comments "
				+ " WHERE dest_userid = :userid ORDER BY created_at DESC";
		Query idQuery = em.createNativeQuery(idQueryString);
		idQuery.setParameter("userid", userid);
		idQuery.setFirstResult((pageNum - 1) * MAX_ROWS);
		idQuery.setMaxResults(MAX_ROWS);
		
		final List<Long> ids = idQuery.getResultList();
		
		if(ids.isEmpty())
			return Collections.emptyList();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<UserComment> cq = cb.createQuery(UserComment.class);
		Root<UserComment> from = cq.from(UserComment.class);
		from.fetch("commenter", JoinType.LEFT);
		from.fetch("receiver", JoinType.LEFT);
		final TypedQuery<UserComment> query = em.createQuery(
				cq.select(from).where(from.get("commentid").in(ids)).distinct(true).orderBy(cb.desc(from.get("createdAt")))
			);
		
		return query.getResultList();
	}

	@Override
	public User create(String username, String firstname, String lastname, String password, Role role)
			throws UserAlreadyExistsException {
		final User user = new User(username, firstname, lastname, password, role, Instant.now());
		try {
			em.persist(user);
			em.flush();
		} catch(PersistenceException e) {
			throw new UserAlreadyExistsException();
		}
		return user;
	}
	
	@Override
	public int countByUserComments(final long userid) {
		String queryString = "SELECT count(uc) FROM UserComment AS uc WHERE "
				+ " uc.receiver.userid = :userid";
		
		TypedQuery<Long> query = em.createQuery(queryString, Long.class);
		query.setParameter("userid", userid);
		
		return query.getSingleResult().intValue();
	}
	
	@Override
	public int getCommentsPageInitIndex(final int pageNum) {
		return (pageNum - 1) * MAX_ROWS + 1;
	}

	@Override
	public int getCommentsMaxPage(final long userid) {
		int rows = countByUserComments(userid);
		int pageCount = rows / MAX_ROWS;
		if(rows % MAX_ROWS != 0)
			pageCount += 1;
		return pageCount;
	}

}
