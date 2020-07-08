package ar.edu.itba.paw.webapp.controller;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
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
import ar.edu.itba.paw.exception.EntityNotFoundException;
import ar.edu.itba.paw.exception.EventCreationException;
import ar.edu.itba.paw.exception.EventFullException;
import ar.edu.itba.paw.exception.EventNotFinishedException;
import ar.edu.itba.paw.exception.InscriptionClosedException;
import ar.edu.itba.paw.exception.UserBusyException;
import ar.edu.itba.paw.exception.UserNotAuthorizedException;
import ar.edu.itba.paw.interfaces.EmailService;
import ar.edu.itba.paw.interfaces.EventService;
import ar.edu.itba.paw.interfaces.PitchService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Inscription;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.dto.EventDto;
import ar.edu.itba.paw.webapp.dto.FullEventDto;
import ar.edu.itba.paw.webapp.dto.InscriptionCollectionDto;
import ar.edu.itba.paw.webapp.dto.InscriptionDto;
import ar.edu.itba.paw.webapp.dto.form.EventForm;
import ar.edu.itba.paw.webapp.dto.form.validator.FormValidator;
import ar.edu.itba.paw.webapp.exception.EventNotFoundException;
import ar.edu.itba.paw.webapp.exception.FormValidationException;
import ar.edu.itba.paw.webapp.exception.PitchNotFoundException;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;

@Path("pitches/{pitchId}/events")
@Component
@Produces(value = { MediaType.APPLICATION_JSON })
public class EventController extends BaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(EventController.class);
	private static final String TIME_ZONE = "America/Buenos_Aires";
	
	@Context
	private	UriInfo	uriInfo;

	@Autowired
	private EventService es;

    @Autowired
    private EmailService ems;

    @Autowired
    private UserService us;
    
    @Autowired
    private PitchService ps;
    
	@Autowired
	private FormValidator validator;

    @GET
    @Path("/{id}")
    public Response retrieveElement(@PathParam("id") long id, @PathParam("pitchId") long pitchid)
    		throws EntityNotFoundException {
    	ps.findById(pitchid).orElseThrow(PitchNotFoundException::new);

    	Event event = es.findByEventId(id).orElseThrow(EventNotFoundException::new);
    	List<Inscription> inscriptions = event.getInscriptions();
	    User current = loggedUser();

        boolean isParticipant = false;
        if (loggedUser() != null) {
        	Long count = inscriptions.stream()
        		.filter(x -> x.getInscriptedUser().equals(current))
        		.collect(Collectors.counting());
        	isParticipant = count > 0;
        }
        int voteBalance = es.getVoteBalance(event.getEventId());
        int userVote = 0;
        if (loggedUser() != null) {
        	userVote = es.getUserVote(event.getEventId(), current.getUserid());
        }
        
        return Response.ok(FullEventDto.ofEvent(
        		event, isParticipant, voteBalance, userVote)
        		).build();
    }

    @POST
    @Path("/{id}/join")
    public Response joinEvent(@PathParam("id") long id, @PathParam("pitchId") long pitchid)
    	throws EntityNotFoundException, InscriptionClosedException, EventFullException, UserBusyException {
    	ps.findById(pitchid).orElseThrow(PitchNotFoundException::new);
    	es.findByEventId(id).orElseThrow(EventNotFoundException::new);

    	es.joinEvent(loggedUser().getUserid(), id);
        return Response.status(Status.NO_CONTENT).build();
    }


    @POST
    @Path("/{id}/leave")
    public Response leaveEvent(@PathParam("id") long id, @PathParam("pitchId") long pitchid)
    		throws DateInPastException, EntityNotFoundException {
    	ps.findById(pitchid).orElseThrow(PitchNotFoundException::new);
    	es.findByEventId(id).orElseThrow(EventNotFoundException::new);
    	loggedUser().getUserid();
		es.leaveEvent(id, loggedUser().getUserid());
        return Response.status(Status.NO_CONTENT).build();
    }

    @POST
    @Path("/{id}/kick-user/{userId}")
    public Response kickUserFromEvent(
    		@PathParam("id") long eventid,
    		@PathParam("pitchId") long pitchid,
    		@PathParam("userId") long kickedUserId)
    		throws UserNotAuthorizedException, EntityNotFoundException, DateInPastException {
    	ps.findById(pitchid).orElseThrow(PitchNotFoundException::new);
    	Event event = es.findByEventId(eventid).orElseThrow(EventNotFoundException::new);
    	User kicked = us.findById(kickedUserId).orElseThrow(UserNotFoundException::new);
    	es.kickFromEvent(loggedUser(), kickedUserId, event);
    	ems.youWereKicked(kicked, event, LocaleContextHolder.getLocale());
    	return Response.status(Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("/{id}")
	public Response cancelEvent(@PathParam("id") final long id, @PathParam("pitchId") long pitchid)
			throws EntityNotFoundException, UserNotAuthorizedException, DateInPastException {
    	
    	ps.findById(pitchid).orElseThrow(PitchNotFoundException::new);
		Event event = es.findByEventId(id).orElseThrow(EventNotFoundException::new);
		
		List<User> inscriptedUsers = event.getInscriptions().stream().map(i -> i.getInscriptedUser()).collect(Collectors.toList());
		es.cancelEvent(event, loggedUser().getUserid());
		
		for(User inscriptedUser : inscriptedUsers) {
			if(inscriptedUser != event.getOwner())
				ems.eventCancelled(inscriptedUser, event, LocaleContextHolder.getLocale());
		}
		LOGGER.debug("Deleted event with id {}", id);
		return Response.status(Status.NO_CONTENT).build();
	}


    @POST
    @Path("/{eventId}/upvote")
    public Response upvote(@PathParam("eventId") final long eventid, @PathParam("pitchId") long pitchid)
    	throws EntityNotFoundException, UserNotAuthorizedException, EventNotFinishedException {
    	ps.findById(pitchid).orElseThrow(PitchNotFoundException::new);
    	Event ev = es.findByEventId(eventid).orElseThrow(EventNotFoundException::new);
    	es.vote(true, ev, loggedUser().getUserid());
    	return Response.status(Status.NO_CONTENT).build();
    }


    @POST
    @Path("/{eventId}/downvote")
    public Response downvote(@PathParam("eventId") final long eventid, @PathParam("pitchId") long pitchid)
    	throws EntityNotFoundException, UserNotAuthorizedException, EventNotFinishedException {
    	ps.findById(pitchid).orElseThrow(PitchNotFoundException::new);
    	Event ev = es.findByEventId(eventid).orElseThrow(EventNotFoundException::new);
    	es.vote(false, ev, loggedUser().getUserid());
    	return Response.status(Status.NO_CONTENT).build();
    }
    
    
    @GET
    @Path("/{id}/inscriptions")
    public Response getInscriptions(@PathParam("id") final long eventid, @PathParam("pitchId") long pitchid)
    		throws EntityNotFoundException {
    	ps.findById(pitchid).orElseThrow(PitchNotFoundException::new);
    	Event ev = es.findByEventId(eventid).orElseThrow(EventNotFoundException::new);
    	return Response.ok(InscriptionCollectionDto.ofInscriptions(ev.getInscriptions().stream()
    			.map(InscriptionDto::ofInscription).collect(Collectors.toList()))).build();
    }
    
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response createEvent(
    		@PathParam("pitchId") long pitchId,
    		@FormDataParam("eventForm") final EventForm form)
    		 throws PitchNotFoundException, FormValidationException, EventCreationException, DateInPastException {
    	if(form == null) {
    		return Response.status(Status.BAD_REQUEST).build();
    	}
    	
    	Instant eventDate = tryInstantStartOfDay(form.getDate(), TIME_ZONE);
    	Instant inscriptionEndDate = tryDateTimeToInstant(form.getInscriptionEndDate(), TIME_ZONE);
    	
    	LOGGER.debug("date: {}, eventDate: {}", eventDate, eventDate);
    	LOGGER.debug("inscription: {}, inscriptionDate: {}", form.getInscriptionEndDate(), inscriptionEndDate);
    	
    	validator.validate(form);
    	
    	Pitch p = ps.findById(pitchId).orElseThrow(PitchNotFoundException::new);
    	Event ev = null;
    	
    	ev = es.create(form.getName(), loggedUser(), p, form.getDescription(),
    			form.getMaxParticipants(), eventDate, form.getStartsAtHour(),
    			form.getEndsAtHour(), inscriptionEndDate);
    	
	    final URI uri = uriInfo.getAbsolutePathBuilder()
	    		.path(String.valueOf(ev.getEventId())).build();
    	return Response.created(uri).entity(EventDto.ofEvent(ev, false)).build();
    }


//	@ExceptionHandler({ EventNotFoundException.class })
//	private ModelAndView eventNotFound() {
//		return new ModelAndView("404");
//	}
//
//
//	@ExceptionHandler({ PitchNotFoundException.class })
//	private ModelAndView pitchNotFound() {
//		return new ModelAndView("404");
//	}
//
//
//	@ExceptionHandler({ EventNotFinishedException.class })
//	private ModelAndView eventNotFinished() {
//		return new ModelAndView("404");
//	}
//
//
//	@ExceptionHandler({ UserNotFoundException.class })
//	private ModelAndView userNotFound() {
//		return new ModelAndView("404");
//	}

}
