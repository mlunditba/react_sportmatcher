package ar.edu.itba.paw.webapp.exception.handler;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import ar.edu.itba.paw.exception.EventNotFinishedException;
import ar.edu.itba.paw.webapp.dto.exception.ExceptionDto;

@Provider
public class EventNotFinishedExceptionHandler implements ExceptionMapper<EventNotFinishedException> {

	@Override
	public Response toResponse(EventNotFinishedException exception) {
		return Response
				.status(Status.FORBIDDEN)
				.entity(ExceptionDto.ofException(exception))
				.build();
	}

}
