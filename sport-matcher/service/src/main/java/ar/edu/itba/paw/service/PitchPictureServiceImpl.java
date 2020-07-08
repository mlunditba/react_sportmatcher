package ar.edu.itba.paw.service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.exception.IllegalParamException;
import ar.edu.itba.paw.exception.PictureProcessingException;
import ar.edu.itba.paw.interfaces.PictureService;
import ar.edu.itba.paw.interfaces.PitchPictureDao;
import ar.edu.itba.paw.interfaces.PitchPictureService;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.PitchPicture;

@Service
public class PitchPictureServiceImpl implements PitchPictureService {
	
	@Autowired
	private PitchPictureDao ppd;
	
	@Autowired
	private PictureService ps;
	
	public static final int MAX_WIDTH = 300;
	public static final int MAX_HEIGHT = 300;

	@Override
	public Optional<PitchPicture> findByPitchId(long pitchid) {
		if(pitchid <= 0) {
			throw new IllegalParamException("Id must be greater than zero.");
		}
		return ppd.findByPitchId(pitchid);
	}
	
	@Transactional
	@Override
	public void create(Pitch pitch, byte[] data) throws PictureProcessingException {
		if(data.length == 0)
			return;
		try {
			byte[] convertedPicture = ps.convert(data, MAX_WIDTH, MAX_HEIGHT);
			ppd.create(pitch, convertedPicture);
		} catch(IllegalParamException | IOException e) {
			throw new PictureProcessingException("PictureProcessingError");
		}
	}

}
