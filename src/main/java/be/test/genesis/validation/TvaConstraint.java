package be.test.genesis.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
 

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {TvaValidator.class})
public @interface TvaConstraint {
	String message() default "{TVA number is required for freelancer}";
    Class<?>[] groups() default {};
    Class<? extends Payload >[] payload() default {};

}
