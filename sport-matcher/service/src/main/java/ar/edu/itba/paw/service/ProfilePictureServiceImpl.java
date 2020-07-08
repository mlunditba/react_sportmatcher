package ar.edu.itba.paw.service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.exception.IllegalParamException;
import ar.edu.itba.paw.exception.PictureProcessingException;
import ar.edu.itba.paw.interfaces.PictureService;
import ar.edu.itba.paw.interfaces.ProfilePictureDao;
import ar.edu.itba.paw.interfaces.ProfilePictureService;
import ar.edu.itba.paw.model.ProfilePicture;
import ar.edu.itba.paw.model.User;

@Service
public class ProfilePictureServiceImpl implements ProfilePictureService {
	
	@Autowired
	private ProfilePictureDao ppd;
	
	@Autowired
	private PictureService ps;
	
	public static final int MAX_WIDTH = 300;
	public static final int MAX_HEIGHT = 300;

	@Override
	public Optional<ProfilePicture> findByUserId(long userid) {
		if(userid <= 0) {
			throw new IllegalParamException("Id must be greater than zero.");
		}
		return ppd.findByUserId(userid);
	}

	@Transactional
	@Override
	public void create(User user, byte[] data) 
		throws PictureProcessingException {
		if(data.length == 0)
			return;
		try {
			byte[] convertedPicture = ps.convert(data, MAX_WIDTH, MAX_HEIGHT);
			ppd.create(user, convertedPicture);
		} catch(IllegalParamException | IOException e) {
			throw new PictureProcessingException("PictureProcessingError");
		}
	}

}
