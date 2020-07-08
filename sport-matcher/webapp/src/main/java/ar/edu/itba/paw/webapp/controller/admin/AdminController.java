package ar.edu.itba.paw.webapp.controller.admin;

import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import ar.edu.itba.paw.exception.DateInPastException;
import ar.edu.itba.paw.exception.EntityNotFoundException;
import ar.edu.itba.paw.interfaces.EmailService;
import ar.edu.itba.paw.interfaces.EventService;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.controller.BaseController;
import ar.edu.itba.paw.webapp.exception.EventNotFoundException;

@Path("admin/events")
@Component
@Produces(value = { MediaType.APPLICATION_JSON })
public class AdminController extends BaseController {
	
	@Context
	private	UriInfo	uriInfo;

	@Autowired
	private EventService es;
	
	@Autowired
    private EmailService ems;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

	@DELETE
	@Path("/{id}")
	public Response deleteEvent(@PathParam("id") final long id)
			throws DateInPastException, EntityNotFoundException {
		Event event = es.findByEventId(id).orElseThrow(EventNotFoundException::new);
		
		List<User> inscriptedUsers = event.getInscriptions().stream().map(i -> i.getInscriptedUser()).collect(Collectors.toList());
		es.deleteEvent(id);
		for(User inscriptedUser : inscriptedUsers) {
			ems.eventCancelled(inscriptedUser, event, LocaleContextHolder.getLocale());
		}
		if(!inscriptedUsers.contains(event.getOwner())) {
			ems.eventCancelled(event.getOwner(), event, LocaleContextHolder.getLocale());
		}
		
		LOGGER.debug("Deleted event with id {}", id);
		return Response.status(Status.NO_CONTENT).build();
	}
	
	
//	@ExceptionHandler({ EventNotFoundException.class })
//	public ModelAndView eventNotFound() {
//		return new ModelAndView("404");
//	}

}
