package ar.edu.itba.paw.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "pitch_pictures")
public class PitchPicture {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pitch_pictures_pitch_picture_id_seq")
	@SequenceGenerator(sequenceName = "pitch_pictures_pitch_picture_id_seq", name = "pitch_pictures_pitch_picture_id_seq", allocationSize = 1)
	@Column(name = "pitch_picture_id")
	private long pitchPictureId;
	
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "pitchid")
	private Pitch belongsTo;
	
	@Column(name = "data", nullable = false)
	private byte[] data;
	
	/*package*/ PitchPicture() {
		
	}
	
	public PitchPicture(Pitch belongsTo) {
		super();
		this.belongsTo = belongsTo;
	}
	
	public PitchPicture(Pitch belongsTo, byte[] data) {
		this(belongsTo);
		this.data = data;
	}
	
	@Override
	public String toString() {
		return "Pitch Picture id: " + pitchPictureId + " Pitch id: " + belongsTo.getPitchid();
	}

	public long getPitchPictureId() {
		return pitchPictureId;
	}

	public Pitch getBelongsTo() {
		return belongsTo;
	}

	public byte[] getData() {
		return data;
	}

}
