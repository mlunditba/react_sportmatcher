package ar.edu.itba.paw.webapp.dto;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import ar.edu.itba.paw.model.Sport;

public class SportCollectionDto {
	
	private List<String> sports;
	
	public static SportCollectionDto ofSports(Sport[] sports) {
		SportCollectionDto dto = new SportCollectionDto();
		dto.sports = Arrays.asList(sports)
				.stream()
				.map(String::valueOf)
				.collect(Collectors.toList());

		return dto;
	}

	public List<String> getSports() {
		return sports;
	}

	public void setSports(List<String> sports) {
		this.sports = sports;
	}

}
