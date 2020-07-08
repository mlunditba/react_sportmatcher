package ar.edu.itba.paw.persistence;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.interfaces.PitchPictureDao;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.PitchPicture;

@Repository
public class PitchPictureHibernateDao implements PitchPictureDao {
	
	@PersistenceContext
	private EntityManager em;

	@Override
	public Optional<PitchPicture> findByPitchId(long pitchid) {
		TypedQuery<PitchPicture> query = em.createQuery("From PitchPicture as pp where pp.belongsTo.id = :pitchid", PitchPicture.class);
		query.setParameter("pitchid", pitchid);
		return query.getResultList().stream().findFirst();
	}

	@Override
	public void create(Pitch pitch, byte[] data) {
		final PitchPicture pp = new PitchPicture(pitch, data);
		em.persist(pp);
	}

}
