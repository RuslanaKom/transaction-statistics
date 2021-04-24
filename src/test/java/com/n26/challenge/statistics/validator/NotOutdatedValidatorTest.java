package com.n26.challenge.statistics.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
public class NotOutdatedValidatorTest {
    private static final long AGE_LIMIT_IN_SECONDS = 60L;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    private NotOutdatedValidator notOutdatedValidator;

    @BeforeEach
    public void setup(){
        notOutdatedValidator = new NotOutdatedValidator(AGE_LIMIT_IN_SECONDS);
    }

    @Test
    public void shouldReturnTrue() {
        // given
        LocalDateTime timeToValidate = LocalDateTime.now().minusSeconds(AGE_LIMIT_IN_SECONDS).plusSeconds(5);

        // when
        Boolean result = notOutdatedValidator.isValid(timeToValidate, constraintValidatorContext);

        //then
        assertThat(result, is(true));
    }

    @Test
    public void shouldReturnFalse() {
        // given
        LocalDateTime timeToValidate = LocalDateTime.now().minusSeconds(AGE_LIMIT_IN_SECONDS).minusSeconds(1);

        // when
        Boolean result = notOutdatedValidator.isValid(timeToValidate, constraintValidatorContext);

        //then
        assertThat(result, is(false));
    }
}
