package ar.edu.itba.paw.webapp.dto.form;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;

public class PictureForm {
	
	@FormDataParam("picture")
	private FormDataBodyPart fileBodyPart;
	
	public FormDataBodyPart getFileBodyPart() {
		return fileBodyPart;
	}
	
	public void setFileBodyPart(FormDataBodyPart fileBodyPart) {
		this.fileBodyPart = fileBodyPart;
	}
	
	public byte[] getBytes() {
		if(fileBodyPart == null)
			return null;
		return fileBodyPart.getValueAs(byte[].class);
	}

}
