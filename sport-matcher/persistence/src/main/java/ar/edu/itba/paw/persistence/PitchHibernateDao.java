package ar.edu.itba.paw.persistence;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.interfaces.PitchDao;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.Sport;

@Repository
public class PitchHibernateDao implements PitchDao {
	
	private static final int MAX_ROWS = 10;
	
	@PersistenceContext
	private EntityManager em;

	@Override
	public Optional<Pitch> findById(long pitchid) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Pitch> cq = cb.createQuery(Pitch.class);
		Root<Pitch> from = cq.from(Pitch.class);
		
		final TypedQuery<Pitch> query = em.createQuery(
				cq.select(from).where(cb.equal(from.get("pitchid"), pitchid))
			);
		
		return query.getResultList().stream().findFirst();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Pitch> findByClubId(long clubid, int page) {
		Query idQuery = em.createNativeQuery("SELECT pitchid FROM pitches WHERE clubid = :clubid ");
		idQuery.setParameter("clubid", clubid);
		idQuery.setFirstResult((page - 1) * MAX_ROWS);
		idQuery.setMaxResults(MAX_ROWS);
		final List<Long> ids = idQuery.getResultList();
		
		if(ids.isEmpty())
			return Collections.emptyList();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Pitch> cq = cb.createQuery(Pitch.class);
		Root<Pitch> from = cq.from(Pitch.class);
		
		final TypedQuery<Pitch> query = em.createQuery(
				cq.select(from).where(from.get("pitchid").in(ids)).distinct(true)
			);
		
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Pitch> findBy(Optional<String> pitchName, Optional<String> sport, Optional<String> location,
			Optional<String> clubName, int pageNum) {
		
		Map<String, Object> paramsMap = new HashMap<>();
		StringBuilder idQueryString = new StringBuilder("SELECT pitchid ");
		idQueryString.append(getFilterQueryEndString(paramsMap, pitchName, sport, location, clubName));
		idQueryString.append(" ORDER BY pitchname ASC ");
		
		Query idQuery = em.createNativeQuery(idQueryString.toString());
		for(Map.Entry<String, Object> entry : paramsMap.entrySet()) {
			idQuery.setParameter(entry.getKey(), entry.getValue());
		}
		idQuery.setFirstResult((pageNum - 1) * MAX_ROWS);
		idQuery.setMaxResults(MAX_ROWS);
		final List<Long> ids = idQuery.getResultList();
		
		if(ids.isEmpty())
			return Collections.emptyList();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Pitch> cq = cb.createQuery(Pitch.class);
		Root<Pitch> from = cq.from(Pitch.class);
		
		final TypedQuery<Pitch> query = em.createQuery(
				cq.select(from).where(from.get("pitchid").in(ids)).distinct(true)
			);
		
		return query.getResultList();
	}

	@Override
	public int countFilteredPitches(Optional<String> pitchName, Optional<String> sport, 
			Optional<String> location, Optional<String> clubName) {

		Map<String, Object> paramsMap = new HashMap<>();
		StringBuilder idQueryString = new StringBuilder("SELECT pitchid ");
		idQueryString.append(getFilterQueryEndString(paramsMap, pitchName, sport, location, clubName));
		
		Query idQuery = em.createNativeQuery(idQueryString.toString());
		for(Map.Entry<String, Object> entry : paramsMap.entrySet()) {
			idQuery.setParameter(entry.getKey(), entry.getValue());
		}
		
		return idQuery.getResultList().size();
	}
	
	private String getFilterQueryEndString(Map<String, Object> paramsMap, 
			final Optional<String> pitchName, final Optional<String> sport,
			final Optional<String> location, final Optional<String> clubName) {
		Filter[] params = { 
				new Filter("pitchname", pitchName),
				new Filter("sport", sport),
				new Filter("location", location),
				new Filter("clubname", clubName)
		};
		StringBuilder queryString = new StringBuilder(" FROM pitches NATURAL JOIN clubs ");
		for(Filter param : params) {
			if(param.getValue().isPresent() && !isEmpty(param.getValue())) {
				int paramNum = paramsMap.size();
				queryString.append(buildPrefix(paramNum));
				queryString.append(param.queryAsString(paramNum));
				paramsMap.put(Filter.getParamName() + paramNum, param.getValue().get());
			}
		}
		return queryString.toString();
	}
	
	private boolean isEmpty(Optional<?> opt) {
		return opt.get().toString().isEmpty();
	}
	
	private String buildPrefix(int currentFilter) {
		if(currentFilter == 0)
			return " WHERE ";
		return " AND ";
	}

	@Override
	public int countPitchPages(final int totalPitchQty) {
		int pageCount = totalPitchQty / MAX_ROWS;
		if(totalPitchQty % MAX_ROWS != 0)
			pageCount += 1;
		
		return pageCount;
	}

	@Override
	public Pitch create(Club club, String name, Sport sport) {
		final Pitch pitch = new Pitch(club, name, sport, Instant.now());
		em.persist(pitch);
		return pitch;
	}

	@Override
	public int getPageInitialPitchIndex(int pageNum) {
		return (pageNum -1) * MAX_ROWS + 1;
	}

	@Override
	public void deletePitch(long pitchid) {
		Pitch pitch = em.find(Pitch.class, pitchid);
		em.remove(pitch);
	}

	@Override
	public int countByClubId(long clubid) {
		final String queryString = "SELECT count(*) FROM Pitch AS p WHERE p.club.clubid = :clubid";
		TypedQuery<Long> query = em.createQuery(queryString.toString(), Long.class);
		query.setParameter("clubid", clubid);
		
		return query.getSingleResult().intValue();
	}

}
