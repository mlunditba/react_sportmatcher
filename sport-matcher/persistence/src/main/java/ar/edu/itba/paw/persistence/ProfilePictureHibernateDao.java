package ar.edu.itba.paw.persistence;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.interfaces.ProfilePictureDao;
import ar.edu.itba.paw.model.ProfilePicture;
import ar.edu.itba.paw.model.User;

@Repository
public class ProfilePictureHibernateDao implements ProfilePictureDao {
	
	@PersistenceContext
	private EntityManager em;

	@Override
	public Optional<ProfilePicture> findByUserId(long userid) {
		TypedQuery<ProfilePicture> query = em.createQuery("From ProfilePicture as pp where pp.addedBy.id = :userid", ProfilePicture.class);
		query.setParameter("userid", userid);
		return query.getResultList().stream().findFirst();
	}

	@Override
	public void create(User user, byte[] data) {
		final ProfilePicture pp = new ProfilePicture(user, data);
		em.persist(pp);
	}

}
