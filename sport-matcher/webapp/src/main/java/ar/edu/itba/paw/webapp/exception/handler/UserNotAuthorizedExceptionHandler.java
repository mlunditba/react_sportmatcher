package ar.edu.itba.paw.webapp.exception.handler;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import ar.edu.itba.paw.exception.UserNotAuthorizedException;
import ar.edu.itba.paw.webapp.dto.exception.ExceptionDto;

@Provider
public class UserNotAuthorizedExceptionHandler implements ExceptionMapper<UserNotAuthorizedException> {

	@Override
	public Response toResponse(UserNotAuthorizedException exception) {
		return Response
				.status(Status.FORBIDDEN)
				.entity(ExceptionDto.ofException(exception))
				.build();
	}

}
