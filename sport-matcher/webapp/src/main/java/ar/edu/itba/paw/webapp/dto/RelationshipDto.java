package ar.edu.itba.paw.webapp.dto;

public class RelationshipDto {
	
	private boolean relationship;
	
	public static RelationshipDto ofRelationship(boolean relationship) {
		RelationshipDto dto = new RelationshipDto();
		dto.relationship = relationship;
		
		return dto;
	}

	public boolean isRelationship() {
		return relationship;
	}

	public void setRelationship(boolean relationship) {
		this.relationship = relationship;
	}

}
