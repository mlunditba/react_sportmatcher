package ar.edu.itba.paw.webapp.exception.handler;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import ar.edu.itba.paw.exception.UserAlreadyExistsException;
import ar.edu.itba.paw.webapp.dto.exception.ExceptionDto;

@Provider
public class UserAlreadyExistsExceptionHandler implements ExceptionMapper<UserAlreadyExistsException> {

	@Override
	public Response toResponse(UserAlreadyExistsException exception) {
		return Response
				.status(Status.CONFLICT)
				.entity(ExceptionDto.ofException(exception))
				.build();
	}

}
