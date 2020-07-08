package ar.edu.itba.paw.webapp.dto.form;

import ar.edu.itba.paw.webapp.dto.form.validator.IntegerRange;

public class TournamentResultForm {

	@IntegerRange
	private Integer firstResult;
	
	@IntegerRange
	private Integer secondResult;

	public Integer getFirstResult() {
		return firstResult;
	}

	public void setFirstResult(Integer firstResult) {
		this.firstResult = firstResult;
	}

	public Integer getSecondResult() {
		return secondResult;
	}

	public void setSecondResult(Integer secondResult) {
		this.secondResult = secondResult;
	}
	
}
