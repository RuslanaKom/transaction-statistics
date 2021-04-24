package com.n26.challenge.statistics.validator;

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
public class NotFutureValidatorTest {

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @InjectMocks
    private NotFutureValidator notFutureValidator;

    @Test
    public void shouldReturnTrue() {
        // given
        LocalDateTime timeToValidate = LocalDateTime.now();

        // when
        Boolean result = notFutureValidator.isValid(timeToValidate, constraintValidatorContext);

        //then
        assertThat(result, is(true));
    }

    @Test
    public void shouldReturnFalse() {
        // given
        LocalDateTime timeToValidate = LocalDateTime.now().plusSeconds(2);

        // when
        Boolean result = notFutureValidator.isValid(timeToValidate, constraintValidatorContext);

        //then
        assertThat(result, is(false));
    }
}
