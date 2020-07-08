package ar.edu.itba.paw.interfaces;

import java.io.IOException;

import ar.edu.itba.paw.exception.IllegalParamException;

public interface PictureService {
	
	/**
	 * Converts a picture, ensuring its size is smaller than 1000x1000
	 * @param picture Picture raw data
	 * @return Converted picture
	 * @throws IOException
	 * @throws IllegalParamException If the image cannot be processed
	 */
	public byte[] convert(byte[] picture) throws IOException, IllegalParamException;
	
	/**
	 * Converts a picture, ensuring its size is smaller than maxWidth x maxHeight
	 * @param picture Picture raw data
	 * @param maxWidth Horizontal resolution
	 * @param maxHeight Vertical resolution
	 * @return Converted picture
	 * @throws IOException If an error occurs handling the picture's bytes
	 * @throws IllegalParamException If the image cannot be processed
	 */
	public byte[] convert(byte[] picture, int maxWidth, int maxHeight) throws IOException, IllegalParamException;

}
