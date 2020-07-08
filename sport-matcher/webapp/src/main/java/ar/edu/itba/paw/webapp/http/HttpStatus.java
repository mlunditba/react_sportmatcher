package ar.edu.itba.paw.webapp.http;

import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;

public enum HttpStatus implements StatusType {
	
	UNPROCESSABLE_ENTITY(422, "Unprocessable Entity");
	
	private int statusCode;
	private Family family;
	private String reasonPhrase;

	HttpStatus(int statusCode, String reasonPhrase) {
		this.statusCode = statusCode;
		this.family = Family.familyOf(statusCode);
		this.reasonPhrase = reasonPhrase;
	}

	@Override
	public int getStatusCode() {
		return statusCode;
	}

	@Override
	public Family getFamily() {
		return family;
	}

	@Override
	public String getReasonPhrase() {
		return reasonPhrase;
	}

}
