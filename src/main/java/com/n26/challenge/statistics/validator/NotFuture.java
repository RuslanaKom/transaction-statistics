package com.n26.challenge.statistics.validator;

import org.springframework.http.HttpStatus;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ReportAsSingleViolation
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotFutureValidator.class)
public @interface NotFuture {

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String message() default "Future transactions are not accepted";

    HttpStatus status() default HttpStatus.UNPROCESSABLE_ENTITY;
}
