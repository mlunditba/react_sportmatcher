package ar.edu.itba.paw.webapp.dto;

import java.util.List;

public class InscriptionCollectionDto {
	
	private List<InscriptionDto> inscriptions;

	public static InscriptionCollectionDto ofInscriptions(List<InscriptionDto> inscriptions) {
		InscriptionCollectionDto dto = new InscriptionCollectionDto();
		dto.inscriptions = inscriptions;
		return dto;
	}
	
	public List<InscriptionDto> getInscriptions() {
		return inscriptions;
	}

	public void setInscriptions(List<InscriptionDto> inscriptions) {
		this.inscriptions = inscriptions;
	}
}
