package ar.edu.itba.paw.webapp.dto.form.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = FutureDateValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface FutureDate {
	String message() default "Date must be present and in the future";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
	boolean startOfDay() default false;
	String timezone() default "America/Buenos_Aires";
}
