package ar.edu.itba.paw.persistence;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.model.Inscription;
import ar.edu.itba.paw.model.InscriptionId;
import ar.edu.itba.paw.model.User;

@Sql("classpath:schema.sql")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class InscriptionHibernateDaoTest {
	
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private InscriptionHibernateDao ihd;
	
	private static final long USERID = 1;
	private static final long EVENTID = 1;
	private static final long OTHER_USERID = 2;
	private static final long DUMMY_USERID = 3;
	private static final Integer VOTE = -1;
	
	@Test
	public void findByIdsTest() {
		final Optional<Inscription> i = ihd.findByIds(EVENTID, USERID);
		Assert.assertTrue(i.isPresent());
		Assert.assertEquals(em.find(User.class, USERID), i.get().getInscriptedUser());
		Assert.assertEquals(VOTE, i.get().getVote());
		Assert.assertNull(i.get().getTournamentTeam());
		//Assert.assertEquals(em.find(Event.class, EVENTID), i.get().getInscriptionEvent());
	}
	
	@Test
	public void getUserVoteTest() {
		final Optional<Integer> vote = ihd.getUserVote(EVENTID, USERID);
		Assert.assertTrue(vote.isPresent());
		Assert.assertEquals(VOTE, vote.get());
	}
	
	@Test
	public void voteBalanceTest() {
		final Optional<Integer> i = ihd.getVoteBalance(EVENTID);
		Assert.assertTrue(i.isPresent());
		Assert.assertEquals(Integer.valueOf(-2), i.get());
	}
	
	@Rollback
	@Test
	public void upvoteTest() {
		int ret = ihd.vote(true, EVENTID, USERID);
		Assert.assertEquals(1, ret);
		Assert.assertEquals(Integer.valueOf(1),
				ihd.findByIds(EVENTID, USERID).get().getVote());
	}
	
	@Rollback
	@Test
	public void downvoteTest() {
		int ret = ihd.vote(false, EVENTID, USERID);
		Assert.assertEquals(1, ret);
		Assert.assertEquals(Integer.valueOf(-1),
				ihd.findByIds(EVENTID, USERID).get().getVote());
	}
	
	@Rollback
	@Test
	public void voteWithoutInscriptionTest() {
		try {
			em.remove(em.find(Inscription.class, new InscriptionId(EVENTID, USERID)));
		} catch(Exception e) {
			// inscription did not exist
		}
		int ret = ihd.vote(true, EVENTID, USERID);
		Assert.assertEquals(0, ret);
	}
	
	@Test
	public void haveRelationshipTest() {
		Assert.assertTrue(ihd.haveRelationship(
				em.find(User.class, USERID), em.find(User.class, OTHER_USERID)));
	}
	
	@Test
	public void haveRelationshipNonExistentTest() {
		Assert.assertFalse(ihd.haveRelationship(
				em.find(User.class, USERID), em.find(User.class, DUMMY_USERID)));
	}

}
