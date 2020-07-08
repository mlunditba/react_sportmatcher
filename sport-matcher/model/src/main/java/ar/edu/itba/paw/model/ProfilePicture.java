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
@Table(name = "profile_pictures")
public class ProfilePicture {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "profile_pictures_profile_picture_id_seq")
	@SequenceGenerator(sequenceName = "profile_pictures_profile_picture_id_seq", name = "profile_pictures_profile_picture_id_seq", allocationSize = 1)
	@Column(name = "profile_picture_id")
	private long profilePictureId;
	
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "userid")
	private User addedBy;
	
	@Column(name = "data", nullable = false)
	private byte[] data;
	
	/*package*/ ProfilePicture() {
		
	}
	
	public ProfilePicture(User addedBy) {
		super();
		this.addedBy = addedBy;
	}
	
	public ProfilePicture(User addedBy, byte[] data) {
		this(addedBy);
		this.data = data;
	}
	
	@Override
	public String toString() {
		return "Profile Picture id: " + profilePictureId + " User id: " + addedBy.getUserid();
	}

	public long getProfilePictureId() {
		return profilePictureId;
	}

	public User getAddedBy() {
		return addedBy;
	}

	public byte[] getData() {
		return data;
	}

}
