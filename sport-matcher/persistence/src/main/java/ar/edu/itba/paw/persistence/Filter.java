package ar.edu.itba.paw.persistence;

import java.util.Optional;

public class Filter {
	
	private String name;
	private final Optional<?> value;
	
	public Filter(final String name, final Optional<?> value) {
		this.name = name;
		this.value = value;
	}
	
	protected String queryAsString(int paramNum) {
		return " LOWER(" + name.toString() + ") LIKE '%' || LOWER(:" + getParamName() + paramNum + ") || '%' ";
	}
	
	protected String queryAsInteger() {
		return " " + name.toString() + " = ? ";
	}
	
	/*protected String queryAsGreaterInteger(boolean alsoEquals) {
		return " " + name.toString() + " >" + ((alsoEquals) ? "= ?" : " ?");
	}*/
	
	protected String queryAsDateRange(int paramNum, boolean onlyAtDate) {
		return " CAST( " + name.toString() + " AS DATE) " + ((onlyAtDate)? "" : ">") + 
				"= CAST(:" + getParamName() + paramNum + " AS DATE) ";
	}
	
	public String getName() {
		return name;
	}
	
	public Optional<?> getValue() {
		return value;
	}
	
	protected static String getParamName() {
		return "param_";
	}

}
