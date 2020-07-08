package ar.edu.itba.paw.interfaces;

import java.util.Optional;

import ar.edu.itba.paw.model.ProfilePicture;
import ar.edu.itba.paw.model.User;

public interface ProfilePictureDao {
	
	public Optional<ProfilePicture> findByUserId(long userid);
	
	public void create(User user, byte[] data);

}
