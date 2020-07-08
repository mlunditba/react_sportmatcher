package ar.edu.itba.paw.webapp.controller.admin;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import ar.edu.itba.paw.exception.DateInPastException;
import ar.edu.itba.paw.exception.EndsBeforeStartsException;
import ar.edu.itba.paw.exception.EntityNotFoundException;
import ar.edu.itba.paw.exception.EventHasNotEndedException;
import ar.edu.itba.paw.exception.HourOutOfRangeException;
import ar.edu.itba.paw.exception.InscriptionClosedException;
import ar.edu.itba.paw.exception.InsufficientPitchesException;
import ar.edu.itba.paw.exception.InvalidTeamAmountException;
import ar.edu.itba.paw.exception.InvalidTeamSizeException;
import ar.edu.itba.paw.exception.MaximumDateExceededException;
import ar.edu.itba.paw.exception.UnevenTeamAmountException;
import ar.edu.itba.paw.interfaces.ClubService;
import ar.edu.itba.paw.interfaces.EmailService;
import ar.edu.itba.paw.interfaces.TournamentService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.model.Tournament;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.controller.BaseController;
import ar.edu.itba.paw.webapp.dto.TournamentDto;
import ar.edu.itba.paw.webapp.dto.form.TournamentForm;
import ar.edu.itba.paw.webapp.dto.form.TournamentResultForm;
import ar.edu.itba.paw.webapp.dto.form.validator.FormValidator;
import ar.edu.itba.paw.webapp.exception.ClubNotFoundException;
import ar.edu.itba.paw.webapp.exception.FormValidationException;
import ar.edu.itba.paw.webapp.exception.TournamentNotFoundException;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;

@Path("admin/clubs/{clubId}/tournaments")
@Component
@Produces(value = { MediaType.APPLICATION_JSON })
public class AdminTournamentController extends BaseController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AdminTournamentController.class);
	private static final String TIME_ZONE = "America/Buenos_Aires";
	
	@Context
	private	UriInfo	uriInfo;
	
	@Autowired
	private TournamentService ts;
	
	@Autowired
	private ClubService cs;
	
	@Autowired
	private UserService us;
	
	@Autowired
	private EmailService ems;
	
	@Autowired
	private FormValidator validator;
	
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Path("/{id}/events/{eventId}/result")
    public Response setTournamentEventResult(@PathParam("clubId") long clubid,
    		@PathParam("id") long tournamentid, @PathParam("eventId") long eventid,
    		@FormDataParam("tournamentResultForm") final TournamentResultForm form)
    				throws FormValidationException, EventHasNotEndedException, EntityNotFoundException {
		if(form == null) {
    		return Response.status(Status.BAD_REQUEST).build();
    	}
		
    	validator.validate(form);
    	cs.findById(clubid).orElseThrow(ClubNotFoundException::new);
		Tournament tournament = ts.findById(tournamentid).orElseThrow(TournamentNotFoundException::new);

		ts.postTournamentEventResult(tournament, eventid, form.getFirstResult(), form.getSecondResult());
	    return Response.status(Status.NO_CONTENT).build();
	}
    
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response createTournament(@PathParam("clubId") long clubId,
    		@FormDataParam("tournamentForm") final TournamentForm form)
    				throws ClubNotFoundException, FormValidationException, 
    				DateInPastException, MaximumDateExceededException, EndsBeforeStartsException, 
    				HourOutOfRangeException, InvalidTeamAmountException, UnevenTeamAmountException, 
    				InvalidTeamSizeException, InsufficientPitchesException, InscriptionClosedException {
		if(form == null) {
    		return Response.status(Status.BAD_REQUEST).build();
    	}
		
    	Instant firstRoundDate = tryInstantStartOfDay(form.getFirstRoundDate(), TIME_ZONE);
    	Instant inscriptionEndsAt = tryDateTimeToInstant(form.getInscriptionEndDate(), TIME_ZONE);
    	
    	validator.validate(form);
    	
    	Club club = cs.findById(clubId).orElseThrow(ClubNotFoundException::new);
    	Tournament tournament = null;
    	
		/* Only SOCCER Tournaments are supported */
    	tournament = ts.create(form.getName(), Sport.SOCCER, club, form.getMaxTeams(),
    			form.getTeamSize(), firstRoundDate, form.getStartsAtHour(),
    			form.getEndsAtHour(), inscriptionEndsAt, loggedUser());
    	
    	LOGGER.debug("Tournament {} created", tournament);
    	
    	final URI uri = uriInfo.getAbsolutePathBuilder()
	    		.path(String.valueOf(tournament.getTournamentid())).build();
    	return Response.created(uri).entity(TournamentDto.ofTournament(tournament)).build();
    }
    
    @DELETE
    @Path("/{id}")
	public Response deleteTournament(@PathParam("clubId") long clubid, @PathParam("id") final long tournamentid)
			throws InscriptionClosedException, EntityNotFoundException {
    	
    	cs.findById(clubid).orElseThrow(ClubNotFoundException::new);
    	Tournament tournament = ts.findById(tournamentid).orElseThrow(TournamentNotFoundException::new);
    	Map<Long, List<User>> teamsMap = ts.mapTeamMembers(tournamentid);
    	
    	ts.deleteTournament(tournamentid);
		
    	for(List<User> teamMembers : teamsMap.values()) {
			for(User user : teamMembers) {
				ems.tournamentCancelled(user, tournament, LocaleContextHolder.getLocale());
			}
		}
    	
		LOGGER.debug("Deleted tournament with id {}", tournamentid);
		return Response.status(Status.NO_CONTENT).build();
	}
    
    @POST
    @Path("/{id}/kick-user/{userId}")
    public Response kickUserFromTournament(@PathParam("clubId") long clubid,
    		@PathParam("id") long tournamentid, @PathParam("userId") long kickedUserId) 
    				throws InscriptionClosedException, EntityNotFoundException {
    	
    	cs.findById(clubid).orElseThrow(ClubNotFoundException::new);
    	Tournament tournament = ts.findById(tournamentid).orElseThrow(TournamentNotFoundException::new);
    	User kickedUser = us.findById(kickedUserId).orElseThrow(UserNotFoundException::new);
    	
    	ts.kickFromTournament(kickedUser, tournament);
    	ems.youWereKicked(kickedUser, tournament, LocaleContextHolder.getLocale());
    	
    	return Response.status(Status.NO_CONTENT).build();
    }
    
    
//    @ExceptionHandler({ TournamentNotFoundException.class })
//	public ModelAndView tournamentNotFoundHandler() {
//		return new ModelAndView("404");
//	}
	
}
