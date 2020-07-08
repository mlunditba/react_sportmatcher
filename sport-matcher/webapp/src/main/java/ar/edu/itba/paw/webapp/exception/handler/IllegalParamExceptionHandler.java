package ar.edu.itba.paw.webapp.exception.handler;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import ar.edu.itba.paw.exception.IllegalParamException;

@Provider
public class IllegalParamExceptionHandler implements ExceptionMapper<IllegalParamException>{

	@Override
	public Response toResponse(IllegalParamException exception) {
		return Response
				.status(Status.BAD_REQUEST)
				.build();
	}

}
