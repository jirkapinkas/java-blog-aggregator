package cz.jiripinkas.jba.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = { UniqueBlogValidator.class })
public @interface UniqueBlog {

	String message();

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
