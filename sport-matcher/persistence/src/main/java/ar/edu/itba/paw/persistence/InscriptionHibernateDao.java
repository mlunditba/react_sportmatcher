package ar.edu.itba.paw.persistence;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.exception.EntityNotFoundException;
import ar.edu.itba.paw.interfaces.InscriptionDao;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.Inscription;
import ar.edu.itba.paw.model.InscriptionId;
import ar.edu.itba.paw.model.User;

@Repository
public class InscriptionHibernateDao implements InscriptionDao {
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public Optional<Inscription> findByIds(final long eventid, final long userid) {
		return Optional.ofNullable(em.find(Inscription.class, new InscriptionId(eventid, userid)));
	}
	
	@Override
	public Optional<Integer> getVoteBalance(long eventid) {
		StringBuilder queryString = new StringBuilder("SELECT sum(i.vote) FROM Inscription AS i "
				+ " WHERE i.inscriptionEvent.eventid = :eventid ");
		
		TypedQuery<Long> query = em.createQuery(queryString.toString(), Long.class);
		query.setParameter("eventid", eventid);
		
		List<Long> balance = query.getResultList();
		if(balance.isEmpty() || balance.get(0) == null)
			return Optional.empty();
		return Optional.of(balance.get(0).intValue());
	}

	@Override
	public Optional<Integer> getUserVote(long eventid, long userid) {
		String queryString = "SELECT i.vote FROM Inscription AS i "
				+ " WHERE i.inscriptionEvent.eventid = :eventid AND i.inscriptedUser.userid = :userid";
		
		TypedQuery<Integer> query = em.createQuery(queryString, Integer.class);
		query.setParameter("eventid", eventid);
		query.setParameter("userid", userid);
		
		List<Integer> vote = query.getResultList();
		if(vote.isEmpty() || vote.get(0) == null)
			return Optional.empty();
		return Optional.of(vote.get(0).intValue());
	}

	@Override
	public int vote(boolean isUpvote, long eventid, long userid) {
		Query query = em.createQuery("UPDATE Inscription AS i SET i.vote = :vote "
				+ " WHERE i.inscriptionEvent.eventid = :eventid "
				+ " AND i.inscriptedUser.userid = :userid");
		query.setParameter("vote", (isUpvote)? 1 : -1);
		query.setParameter("eventid", eventid);
		query.setParameter("userid", userid);
		return query.executeUpdate();
	}
	
	@Override
	public void deleteInscription(final long eventid, final long userid) throws EntityNotFoundException {
		Inscription inscription = findByIds(eventid, userid)
				.orElseThrow(EntityNotFoundException::new);
		em.createQuery("DELETE FROM Inscription i WHERE i = :inscription").setParameter("inscription", inscription).executeUpdate();
	}

	@Override
	public boolean haveRelationship(final User commenter, final User receiver) {
		String queryString = "SELECT count(i1) FROM Inscription AS i1 "
				+ " WHERE i1.inscriptedUser.userid = :commenterid "
				+ " AND i1.inscriptionEvent.startsAt <= :now AND (EXISTS (FROM Inscription AS i2 "
				+ " WHERE i2.inscriptionEvent.startsAt <= :now "
				+ " AND i2.inscriptionEvent.eventid = i1.inscriptionEvent.eventid "
				+ " AND i2.inscriptedUser.userid = :receiverid) OR EXISTS (FROM Inscription AS i3 "
				+ " WHERE i3.inscriptionEvent.startsAt <= :now "
				+ " AND i3.inscriptionEvent.eventid = i1.inscriptionEvent.eventid "
				+ " AND i3.inscriptionEvent.owner.userid = :receiverid))";
		
		TypedQuery<Long> query = em.createQuery(queryString, Long.class);
		query.setParameter("commenterid", commenter.getUserid());
		query.setParameter("receiverid", receiver.getUserid());
		query.setParameter("now", Instant.now());
		
		return query.getSingleResult().intValue() > 0;
	}
	
	@Override
	public boolean haveRelationship(final User commenter, final Club club) {
		String queryString = "SELECT count(i1) FROM Inscription AS i1 "
				+ " WHERE i1.inscriptionEvent.startsAt <= :now "
				+ " AND ((i1.inscriptedUser.userid = :userid "
				+ " AND i1.inscriptionEvent.pitch.club.clubid = :clubid)"
				+ " OR (i1.inscriptionEvent.owner.userid = :userid "
				+ " AND i1.inscriptionEvent.pitch.club.clubid = :clubid))";
		
		TypedQuery<Long> query = em.createQuery(queryString, Long.class);
		query.setParameter("userid", commenter.getUserid());
		query.setParameter("clubid", club.getClubid());
		query.setParameter("now", Instant.now());
		
		return query.getSingleResult().intValue() > 0;
	}
	
}
