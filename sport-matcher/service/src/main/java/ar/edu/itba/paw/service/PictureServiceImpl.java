package ar.edu.itba.paw.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.imgscalr.Scalr.Mode;
import org.springframework.stereotype.Service;

import ar.edu.itba.paw.exception.IllegalParamException;
import ar.edu.itba.paw.interfaces.PictureService;

@Service
public class PictureServiceImpl implements PictureService {
	
	private static final int DEFAULT_MAX_WIDTH = 1000;
	private static final int DEFAULT_MAX_HEIGHT = 1000;
	private static final String FORMAT = "png";
	private static final int WIDTH = 0;
	private static final int HEIGHT = 1;
	
	@Override
	public byte[] convert(byte[] picture) throws IOException, IllegalParamException {
		return convert(picture, DEFAULT_MAX_WIDTH, DEFAULT_MAX_HEIGHT);
	}

	@Override
	public byte[] convert(byte[] picture, int maxWidth, int maxHeight) throws IOException, IllegalParamException {
		
		if(picture.length == 0 || picture == null)
			return picture;
		BufferedImage img = ImageIO.read(new ByteArrayInputStream(picture));
		
		if(img == null || img.getType() == BufferedImage.TYPE_CUSTOM) {
			throw new IllegalParamException("Unsupported picture format");
		}
		
		int[] dimensions = processImageSize(img.getWidth(), img.getHeight(), maxWidth, maxHeight);
		BufferedImage newImg = Scalr.resize(
				img, Method.ULTRA_QUALITY, Mode.AUTOMATIC, dimensions[WIDTH],
				dimensions[HEIGHT], Scalr.OP_ANTIALIAS);
		
		ByteArrayOutputStream convertedImage = new ByteArrayOutputStream();
		ImageIO.write(newImg, FORMAT, convertedImage);
		img.flush();
		newImg.flush();

		return convertedImage.toByteArray();
	}
	
	private int[] processImageSize(int width, int height, int maxWidth, int maxHeight) {
		int[] dimensions = { width, height };
		if(width > maxWidth)
			dimensions[WIDTH] = maxWidth;
		if(height > maxHeight)
			dimensions[HEIGHT] = maxHeight;
		return dimensions;
	}

}
