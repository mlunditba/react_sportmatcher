package ar.edu.itba.paw.interfaces;

import java.util.Optional;

import ar.edu.itba.paw.exception.IllegalParamException;
import ar.edu.itba.paw.exception.PictureProcessingException;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.PitchPicture;

public interface PitchPictureService {
	
	public Optional<PitchPicture> findByPitchId(long pitchid);
	
	/**
	 * Handles a Pitch's picture before its persistence
	 * @param pitchid 	Id of Pitch the picture belongs to
	 * @param data 		Picture as raw data
	 * @throws IllegalParamException If the raw data is not identified as a picture
	 * @throws PictureProcessingException If there is an error when handling the picture's bytes
	 */
	public void create(Pitch pitch, byte[] data) throws PictureProcessingException, IllegalParamException;
	
}
