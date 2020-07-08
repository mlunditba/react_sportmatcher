package ar.edu.itba.paw.webapp.exception.handler;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import ar.edu.itba.paw.exception.EntityNotFoundException;
import ar.edu.itba.paw.webapp.dto.exception.ExceptionDto;

@Provider
public class EntityNotFoundExceptionHandler implements ExceptionMapper<EntityNotFoundException> {

	@Override
	public Response toResponse(EntityNotFoundException exception) {
		return Response
				.status(Status.NOT_FOUND)
				.entity(ExceptionDto.ofException(exception))
				.build();
	}

}
