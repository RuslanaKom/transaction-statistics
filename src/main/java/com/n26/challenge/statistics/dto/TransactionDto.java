package com.n26.challenge.statistics.dto;

import com.n26.challenge.statistics.validator.NotFuture;
import com.n26.challenge.statistics.validator.NotOutdated;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionDto {

    private BigDecimal amount;

    @NotNull
    @NotOutdated
    @NotFuture
    private LocalDateTime timestamp;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "{\"amount\":\"" + amount.toString() + "\", \"timestamp\":\"" + timestamp.toString() + "\"}";
    }
}
