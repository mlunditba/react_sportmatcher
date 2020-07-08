package ar.edu.itba.paw.webapp.controller;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.edu.itba.paw.exception.EntityNotFoundException;
import ar.edu.itba.paw.exception.InscriptionClosedException;
import ar.edu.itba.paw.exception.TeamAlreadyFilledException;
import ar.edu.itba.paw.exception.UserAlreadyJoinedException;
import ar.edu.itba.paw.exception.UserBusyException;
import ar.edu.itba.paw.interfaces.TournamentService;
import ar.edu.itba.paw.model.Tournament;
import ar.edu.itba.paw.model.TournamentEvent;
import ar.edu.itba.paw.model.TournamentTeam;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.dto.FullTournamentDto;
import ar.edu.itba.paw.webapp.dto.TournamentCollectionDto;
import ar.edu.itba.paw.webapp.dto.TournamentDto;
import ar.edu.itba.paw.webapp.dto.TournamentEventCollectionDto;
import ar.edu.itba.paw.webapp.dto.TournamentEventDto;
import ar.edu.itba.paw.webapp.dto.TournamentTeamCollectionDto;
import ar.edu.itba.paw.webapp.dto.TournamentTeamDto;
import ar.edu.itba.paw.webapp.dto.TournamentTeamInscriptionsCollectionDto;
import ar.edu.itba.paw.webapp.dto.TournamentTeamInscriptionsDto;
import ar.edu.itba.paw.webapp.dto.UserCollectionDto;
import ar.edu.itba.paw.webapp.dto.UserDto;
import ar.edu.itba.paw.webapp.exception.TournamentEventNotFoundException;
import ar.edu.itba.paw.webapp.exception.TournamentNotFoundException;

@Path("tournaments")
@Component
@Produces(value = { MediaType.APPLICATION_JSON })
public class TournamentController extends BaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TournamentController.class);
	
	@Context
	private	UriInfo	uriInfo;
	
	@Autowired
	private TournamentService ts;
	
	@GET
    @Path("/{id}")
    public Response retrieveTournament(@PathParam("id") long tournamentid)
    		throws EntityNotFoundException {

    	Tournament tournament = ts.findById(tournamentid).orElseThrow(TournamentNotFoundException::new);
    	
    	Instant startsAt = ts.findTournamentEventsByRound(tournament.getTournamentid(), 1).get(0).getStartsAt();
    	
    	return Response.ok(FullTournamentDto.ofTournament(tournament, tournament.getRounds(), startsAt)).build();

    }

    @GET
    public Response retrieveTournaments(@QueryParam("pageNum") final int pageNum) {

        List<Tournament> tournaments = ts.findBy(pageNum);
	    
	    int totalTournamentQty = ts.countTournamentTotal();
	    int lastPageNum = ts.countTotalTournamentPages();
        int pageInitialIndex = ts.getPageInitialTournamentIndex(pageNum);

        return Response
        		.status(Status.OK)
        		.entity(TournamentCollectionDto.ofTournaments(
        				tournaments.stream().map(t -> TournamentDto.ofTournament(t)).collect(Collectors.toList()),
        				totalTournamentQty, lastPageNum, pageInitialIndex))
        		.build();
    }
    
    @GET
    @Path("/{id}/inscriptions")
    public Response retrieveTournamentInscriptions(@PathParam("id") long tournamentid)
    		throws EntityNotFoundException {

    	Tournament tournament = ts.findById(tournamentid).orElseThrow(TournamentNotFoundException::new);
    	List<TournamentTeam> teams = tournament.getTeams().stream().map(t ->
    			ts.findByTeamId(t.getTeamid()).get()).collect(Collectors.toList());
    	
    	boolean hasJoined = loggedUser() != null ? 
    			ts.findUserTeam(tournamentid, loggedUser().getUserid()).isPresent() : false;
    	
    	return Response.ok(TournamentTeamInscriptionsCollectionDto.ofTeams(
    			teams.stream().map(t ->
					TournamentTeamInscriptionsDto.ofTeam(
						t,
						ts.getTeamMembers(t).stream().map(UserDto::ofUser).collect(Collectors.toList())
					)
    	    	).collect(Collectors.toList()), hasJoined)
    		).build();
    }

    @POST
    @Path("/{id}/teams/{teamId}/join")
    public Response joinTeam(@PathParam("id") long tournamentid, @PathParam("teamId") long teamid) 
    		throws UserAlreadyJoinedException, InscriptionClosedException,
    			TeamAlreadyFilledException, UserBusyException, EntityNotFoundException {
    	
    	ts.findById(tournamentid).orElseThrow(TournamentNotFoundException::new);
		ts.joinTournament(tournamentid, teamid, loggedUser().getUserid());
        
        LOGGER.debug("User {} joined Tournament {}", loggedUser(), tournamentid);
        
        return Response.status(Status.NO_CONTENT).build();
    }
    
    @POST
    @Path("/{id}/leave")
    public Response leaveTournament(@PathParam("id") long tournamentid) 
    		throws InscriptionClosedException, EntityNotFoundException {
    	
    	ts.findById(tournamentid).orElseThrow(TournamentNotFoundException::new);
		ts.leaveTournament(tournamentid, loggedUser().getUserid());
		
		LOGGER.debug("User {} left Tournament {}", loggedUser(), tournamentid);
        
        return Response.status(Status.NO_CONTENT).build();
    }

    @GET
    @Path("/{id}/events/{eventId}")
    public Response retrieveTournamentEvent(@PathParam("id") long tournamentid,
    		@PathParam("eventId") long eventid) 
    		throws TournamentNotFoundException, TournamentEventNotFoundException {
    	
        ts.findById(tournamentid).orElseThrow(TournamentNotFoundException::new);
        TournamentEvent tournamentEvent = ts.findTournamentEventById(eventid).orElseThrow(TournamentEventNotFoundException::new);
        
        return Response.ok(TournamentEventDto.ofTournamentEvent(
        		tournamentEvent)).build();
    }
    
    @GET
    @Path("/{id}/events/{eventId}/first-team-members")
    public Response retrieveFirstTeamMembers(@PathParam("id") long tournamentid,
    		@PathParam("eventId") long eventid) throws TournamentNotFoundException, TournamentEventNotFoundException {
    	return retrieveTeamMembers(tournamentid, eventid, 1);
    }
    
    @GET
    @Path("/{id}/events/{eventId}/second-team-members")
    public Response retrieveSecondTeamMembers(@PathParam("id") long tournamentid,
    		@PathParam("eventId") long eventid) throws TournamentNotFoundException, TournamentEventNotFoundException {
    	return retrieveTeamMembers(tournamentid, eventid, 2);
    }
    
    private Response retrieveTeamMembers(long tournamentid, long eventid, int teamNumber)
    		throws TournamentNotFoundException, TournamentEventNotFoundException {
    	
    	ts.findById(tournamentid).orElseThrow(TournamentNotFoundException::new);
    	
        TournamentEvent tournamentEvent = ts.findTournamentEventById(eventid).orElseThrow(TournamentEventNotFoundException::new);
        List<User> teamMembers = teamNumber == 1 ? 
        		ts.findTeamMembers(tournamentEvent.getFirstTeam()) : 
        			ts.findTeamMembers(tournamentEvent.getSecondTeam());
        
        return Response.ok(UserCollectionDto.ofUsers(teamMembers.stream()
        		.map(UserDto::ofUser).collect(Collectors.toList()))).build();
    }
    
    @GET
    @Path("/{id}/teams")
    public Response retrieveTournamentTeams(@PathParam("id") long tournamentid)
    		throws TournamentNotFoundException {

    	Tournament tournament = ts.findById(tournamentid).orElseThrow(TournamentNotFoundException::new);
    	
    	return Response.ok(TournamentTeamCollectionDto.ofTeams(
    			tournament.getTeams().stream().map(TournamentTeamDto::ofTeam).collect(Collectors.toList()))
    		).build();
    }
    
    @GET
    @Path("/{id}/round")
    public Response retrieveTournamentRound(@PathParam("id") long tournamentid,
    	@QueryParam("roundPageNum") final Integer roundPage) throws EntityNotFoundException {
    	
    	Tournament tournament = ts.findById(tournamentid).orElseThrow(TournamentNotFoundException::new);
    	
    	int currentRound = ts.getCurrentRound(tournament);
    	Integer roundPageNum = roundPage;
    	if(roundPageNum == null)
    		roundPageNum = currentRound;
    	
        List<TournamentEvent> roundEvents = ts.findTournamentEventsByRound(tournamentid, roundPageNum);
    	
    	return Response.ok(TournamentEventCollectionDto.ofEvents(roundEvents.stream()
    			.map(TournamentEventDto::ofTournamentEvent).collect(Collectors.toList()), roundPageNum, currentRound)
    		).build();
    }

//	@ExceptionHandler({ TournamentEventNotFoundException.class })
//	private ModelAndView tournamentEventNotFound() {
//		return new ModelAndView("404");
//	}

}
