package com.n26.challenge.statistics.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class NotFutureValidator implements ConstraintValidator<NotFuture, LocalDateTime> {
   public void initialize(NotFuture constraint) {
   }

   public boolean isValid(LocalDateTime  timestamp, ConstraintValidatorContext context) {
       if (timestamp == null) {
           return true;
       }
      return !timestamp.isAfter(LocalDateTime.now());
   }
}
