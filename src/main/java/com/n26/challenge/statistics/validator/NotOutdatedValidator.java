package com.n26.challenge.statistics.validator;

import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class NotOutdatedValidator implements ConstraintValidator<NotOutdated, LocalDateTime> {

    private final long ageLimitInSeconds;

    public NotOutdatedValidator(@Value("${transaction.age}") long ageLimitInSeconds) {
        this.ageLimitInSeconds = ageLimitInSeconds;
    }

    public void initialize(NotOutdated constraint) {
    }

    public boolean isValid(LocalDateTime timestamp, ConstraintValidatorContext context) {
        if (timestamp == null) {
            return true;
        }
        return timestamp.plusSeconds(ageLimitInSeconds)
                .isAfter(LocalDateTime.now());
    }
}
