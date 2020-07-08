package ar.edu.itba.paw.interfaces;

import java.util.Optional;

import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.PitchPicture;

public interface PitchPictureDao {
	
	public Optional<PitchPicture> findByPitchId(long pitchid);
	
	public void create(Pitch pitch, byte[] data);
	
}
