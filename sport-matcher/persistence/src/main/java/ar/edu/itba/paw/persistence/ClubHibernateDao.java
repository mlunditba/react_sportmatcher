package ar.edu.itba.paw.persistence;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.interfaces.ClubDao;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.ClubComment;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.model.User;

@Repository
public class ClubHibernateDao implements ClubDao {
	
	private static final int MAX_ROWS = 10;
	private static final String TIME_ZONE = "America/Buenos_Aires";
	
	@PersistenceContext
	private EntityManager em;

	@Override
	public Optional<Club> findById(long clubid) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Club> cq = cb.createQuery(Club.class);
		Root<Club> from = cq.from(Club.class);
		from.fetch("clubPitches", JoinType.LEFT);
		
		final TypedQuery<Club> query = em.createQuery(
				cq.select(from).where(cb.equal(from.get("clubid"), clubid))
			);
		
		return query.getResultList().stream().findFirst();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Club> findAll(int page) {
		Query idQuery = em.createNativeQuery("SELECT clubid FROM clubs ");
		idQuery.setFirstResult((page - 1) * MAX_ROWS);
		idQuery.setMaxResults(MAX_ROWS);
		final List<Long> ids = idQuery.getResultList();
		
		if(ids.isEmpty())
			return Collections.emptyList();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Club> cq = cb.createQuery(Club.class);
		Root<Club> from = cq.from(Club.class);
		
		final TypedQuery<Club> query = em.createQuery(
				cq.select(from).where(from.get("clubid").in(ids)).distinct(true)
			);
		
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Club> findBy(Optional<String> clubName, Optional<String> location, int page) {
		Map<String, Object> paramsMap = new HashMap<>();
		StringBuilder idQueryString = new StringBuilder("SELECT clubid ");
		idQueryString.append(getFilterQueryEndString(paramsMap, clubName, location));
		idQueryString.append(" ORDER BY clubname ASC ");
		
		Query idQuery = em.createNativeQuery(idQueryString.toString());
		for(Map.Entry<String, Object> entry : paramsMap.entrySet()) {
			idQuery.setParameter(entry.getKey(), entry.getValue());
		}
		idQuery.setFirstResult((page - 1) * MAX_ROWS);
		idQuery.setMaxResults(MAX_ROWS);
		final List<Long> ids = idQuery.getResultList();
		
		if(ids.isEmpty())
			return Collections.emptyList();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Club> cq = cb.createQuery(Club.class);
		Root<Club> from = cq.from(Club.class);
		
		final TypedQuery<Club> query = em.createQuery(
				cq.select(from).where(from.get("clubid").in(ids)).distinct(true)
			);
		
		return query.getResultList();
	}

	@Override
	public int countFilteredClubs(Optional<String> clubName, Optional<String> location) {
		Map<String, Object> paramsMap = new HashMap<>();
		StringBuilder idQueryString = new StringBuilder("SELECT clubid ");
		idQueryString.append(getFilterQueryEndString(paramsMap, clubName, location));
		
		Query idQuery = em.createNativeQuery(idQueryString.toString());
		for(Map.Entry<String, Object> entry : paramsMap.entrySet()) {
			idQuery.setParameter(entry.getKey(), entry.getValue());
		}
		
		return idQuery.getResultList().size();
	}
	
	private String getFilterQueryEndString(Map<String, Object> paramsMap, 
			final Optional<String> clubName, final Optional<String> location) {
		Filter[] params = { 
				new Filter("clubname", clubName),
				new Filter("location", location)
		};
		StringBuilder queryString = new StringBuilder(" FROM clubs ");
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
	public Club create(String name, String location) {
		final Club club = new Club(name, location, Instant.now());
		em.persist(club);
		return club;
	}

	@Override
	public int getPageInitialClubIndex(int pageNum) {
		return (pageNum -1) * MAX_ROWS + 1;
	}

	@Override
	public void deleteClub(long clubid) {
		Club club = em.find(Club.class, clubid);
		em.remove(club);
	}

	@Override
	public int countClubPages(final int totalClubQty) {
		int pageCount = totalClubQty / MAX_ROWS;
		if(totalClubQty % MAX_ROWS != 0)
			pageCount += 1;
		
		return pageCount;
	}

	@Override
	public int countPastEvents(long clubid) {
		TypedQuery<Long> query = em.createQuery("SELECT count(e) FROM Event AS e "
				+ " WHERE e.endsAt < :now AND e.pitch.club.clubid = :clubid", Long.class);
		query.setParameter("now", Instant.now());
		query.setParameter("clubid", clubid);
		return query.getSingleResult().intValue();
	}
	
	@Override
	public ClubComment createComment(final User commenter, final Club club, final String comment) {
		
		final ClubComment clubComment = new ClubComment(commenter, club, comment);
		em.persist(clubComment);
		
		return clubComment;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ClubComment> getCommentsByClub(final long clubid, final int pageNum) {
		String idQueryString = "SELECT commentid FROM club_comments "
				+ " WHERE dest_clubid = :clubid ORDER BY created_at DESC";
		Query idQuery = em.createNativeQuery(idQueryString);
		idQuery.setParameter("clubid", clubid);
		idQuery.setFirstResult((pageNum - 1) * MAX_ROWS);
		idQuery.setMaxResults(MAX_ROWS);
		
		final List<Long> ids = idQuery.getResultList();
		
		if(ids.isEmpty())
			return Collections.emptyList();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ClubComment> cq = cb.createQuery(ClubComment.class);
		Root<ClubComment> from = cq.from(ClubComment.class);
		from.fetch("commenter", JoinType.LEFT);
		from.fetch("club", JoinType.LEFT);
		final TypedQuery<ClubComment> query = em.createQuery(
				cq.select(from).where(from.get("commentid").in(ids)).distinct(true)
					.orderBy(cb.desc(from.get("createdAt")))
			);
		
		return query.getResultList();
	}
	
	@Override
	public int countByClubComments(final long clubid) {
		String queryString = "SELECT count(cc) FROM ClubComment AS cc WHERE "
				+ " cc.club.clubid = :clubid";
		
		TypedQuery<Long> query = em.createQuery(queryString, Long.class);
		query.setParameter("clubid", clubid);
		
		return query.getSingleResult().intValue();
	}
	
	@Override
	public int getCommentsPageInitIndex(final int pageNum) {
		return (pageNum - 1) * MAX_ROWS + 1;
	}

	@Override
	public int getCommentsMaxPage(final long clubid) {
		int rows = countByClubComments(clubid);
		int pageCount = rows / MAX_ROWS;
		if(rows % MAX_ROWS != 0)
			pageCount += 1;
		return pageCount;
	}
	
	@Override
	public List<Pitch> getAvailablePitches(final long clubid, final Sport sport, 
			Instant startsAt, Instant endsAt, int amount) {
		String queryString = "FROM Pitch AS p WHERE p.club.clubid = :clubid AND "
				+ " p.sport = :sport AND NOT EXISTS (FROM Event AS e WHERE e.pitch.pitchid = p.pitchid "
				+ " AND ((e.startsAt <= :startsAt AND e.endsAt > :startsAt) "
				+ " OR (e.startsAt > :startsAt AND e.startsAt < :endsAt)))";
		
		TypedQuery<Pitch> query = em.createQuery(queryString, Pitch.class);
		query.setParameter("clubid", clubid);
		query.setParameter("sport", sport);
		query.setParameter("startsAt", startsAt);
		query.setParameter("endsAt", endsAt);
		query.setMaxResults(amount);
		
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Event> findCurrentEventsInClub(final long clubid, final Sport sport) {
		LocalDate ld = LocalDate.now();
		// Today at 00:00
		Instant tomorrow = ld.atStartOfDay().atZone(ZoneId.of(TIME_ZONE)).toInstant().plus(1, ChronoUnit.DAYS);
		// In the following week at 00:00
		Instant inAWeek = tomorrow.plus(7, ChronoUnit.DAYS);
		
		Map<String, Object> paramsMap = new HashMap<>();
		StringBuilder idQueryString = new StringBuilder("(SELECT eventid FROM events NATURAL JOIN pitches "
				+ " WHERE clubid = :clubid AND sport = :sport AND starts_at > :tomorrow AND starts_at < :inAWeek) "
				+ " UNION (SELECT eventid FROM tournament_events NATURAL JOIN pitches WHERE clubid = :clubid AND "
				+ " sport = :sport AND starts_at > :tomorrow AND starts_at < :inAWeek)");
		paramsMap.put("clubid", clubid);
		paramsMap.put("sport", sport.toString());
		paramsMap.put("tomorrow", Timestamp.from(tomorrow));
		paramsMap.put("inAWeek", Timestamp.from(inAWeek));
		
		Query idQuery = em.createNativeQuery(idQueryString.toString());
		for(Map.Entry<String, Object> entry : paramsMap.entrySet()) {
			idQuery.setParameter(entry.getKey(), entry.getValue());
		}
		final List<Long> ids = (List<Long>) idQuery.getResultList();
		
		if(ids.isEmpty())
			return Collections.emptyList();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Event> cq = cb.createQuery(Event.class);
		Root<Event> from = cq.from(Event.class);
		from.fetch("inscriptions", JoinType.LEFT);
		final TypedQuery<Event> query = em.createQuery(
				cq.select(from).where(from.get("eventid").in(ids)).distinct(true)
			);
		
		return query.getResultList();
	}

	@Override
	public Optional<ClubComment> findComment(long commentid) {
		return Optional.ofNullable(em.find(ClubComment.class, commentid));
	}

}
