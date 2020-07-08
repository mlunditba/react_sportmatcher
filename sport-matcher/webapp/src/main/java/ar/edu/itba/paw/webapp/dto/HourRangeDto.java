package ar.edu.itba.paw.webapp.dto;

public class HourRangeDto {
	
	private int minHour;
	private int maxHour;
	
	public static HourRangeDto ofHours(int minHour, int maxHour) {
		final HourRangeDto dto = new HourRangeDto();
		dto.minHour = minHour;
		dto.maxHour = maxHour;
		
		return dto;
	}

	public int getMinHour() {
		return minHour;
	}

	public void setMinHour(int minHour) {
		this.minHour = minHour;
	}

	public int getMaxHour() {
		return maxHour;
	}

	public void setMaxHour(int maxHour) {
		this.maxHour = maxHour;
	}

}
