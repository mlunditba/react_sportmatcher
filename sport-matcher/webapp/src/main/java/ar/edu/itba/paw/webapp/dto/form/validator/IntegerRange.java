package ar.edu.itba.paw.webapp.dto.form.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = IntegerRangeValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface IntegerRange {
	String message() default "Integer must be present and in the given range";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
	int min() default Integer.MIN_VALUE;
	int max() default Integer.MAX_VALUE;
}
