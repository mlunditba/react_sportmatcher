package ar.edu.itba.paw.webapp.dto.form.validator;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FutureDateValidator implements ConstraintValidator<FutureDate, String> {
	
	private boolean startOfDay;
	private String timezone;

	@Override
	public void initialize(FutureDate constraintAnnotation) {
		startOfDay = constraintAnnotation.startOfDay();
		timezone = constraintAnnotation.timezone();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		Instant date;
		if(startOfDay) {
			date = tryInstantStartOfDay(value, timezone);
			return date != null && date.isAfter(LocalDate.now()
					.atStartOfDay(ZoneId.of(timezone)).toInstant());
		}
		date = tryDateTimeToInstant(value, timezone);
		return date != null && date.isAfter(Instant.now().plus(1, ChronoUnit.MINUTES));
	}
	
	private Instant tryInstantStartOfDay(String str, String timezone) {
    	if(str == null || timezone == null)
    		return null;
    	Instant i = null;
    	try {
    		i = LocalDate.parse(str).atStartOfDay(ZoneId.of(timezone)).toInstant();
    	} catch(DateTimeException e) {
    		return null;
    	}
    	return i;
    }
	
    private Instant tryDateTimeToInstant(String str, String timezone) {
    	if(str == null || timezone == null)
    		return null;
    	Instant i = null;
    	try {
    		i = LocalDateTime.parse(str).atZone(ZoneId.of(timezone)).toInstant();
    	} catch(DateTimeException e) {
    		return null;
    	}
    	return i;
    }
}
