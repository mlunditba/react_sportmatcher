package ar.edu.itba.paw.webapp.controller;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.edu.itba.paw.interfaces.EventService;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.webapp.dto.EventCollectionDto;
import ar.edu.itba.paw.webapp.dto.EventDto;
import ar.edu.itba.paw.webapp.exception.PitchNotFoundException;

@Path("events")
@Component
@Produces(value = { MediaType.APPLICATION_JSON })
public class EventCollectionController extends BaseController {
	
	private static final String TIME_ZONE = "America/Buenos_Aires";
	
	@Autowired
	private EventService es;
	
    @GET
    public Response retrieveEvents(@QueryParam("pageNum") @DefaultValue("1") final int pageNum,
                                     @QueryParam("name") String name,
                                     @QueryParam("club") String clubName,
                                     @QueryParam("sport") Sport sport,
                                     @QueryParam("vacancies") String vacancies,
                                     @QueryParam("date") String date) throws PitchNotFoundException {

        Integer vac = tryInteger(vacancies);
    	Instant dateInst = tryInstantStartOfDay(date, TIME_ZONE);
    	if(vac == null && vacancies != null && !vacancies.isEmpty())
    		Response.status(Status.BAD_REQUEST).build();
    	if(dateInst == null && date != null && !date.isEmpty())
    		Response.status(Status.BAD_REQUEST).build();

        List<Event> events = es.findBy(true, Optional.ofNullable(name),
        		Optional.ofNullable(clubName), Optional.ofNullable(sport), Optional.empty(),
        		Optional.ofNullable(vac), Optional.ofNullable(dateInst), pageNum);

        int totalEventQty = es.countFilteredEvents(true, Optional.ofNullable(name),
        		Optional.ofNullable(clubName), Optional.ofNullable(sport), Optional.empty(),
        		Optional.ofNullable(vac), Optional.ofNullable(dateInst));
        int lastPageNum = es.countEventPages(totalEventQty);
        int pageInitialIndex = es.getPageInitialEventIndex(pageNum);

        return Response
        		.status(Status.OK)
        		.entity(EventCollectionDto.ofEvents(
        				events.stream().map(e -> EventDto.ofEvent(e, true)).collect(Collectors.toList()),
        				totalEventQty, lastPageNum, pageInitialIndex))
        		.build();
    }

}
