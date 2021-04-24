package com.n26.challenge.statistics.cache;

import com.n26.challenge.statistics.dto.TransactionDto;
import edu.umd.cs.mtc.MultithreadedTestCase;
import edu.umd.cs.mtc.TestFramework;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TimestampBasedCacheTest {

    private static final long CLEANUP_INTERVAL = 1L;
    private static final long ENTRY_EXPIRATION_TIME = 4L;
    private TimestampBasedCache transactionCache;

    @BeforeEach
    public void setup() {
        transactionCache = new TimestampBasedCache(CLEANUP_INTERVAL, ENTRY_EXPIRATION_TIME);
    }

    @Test
    public void shouldRemoveOutdatedTransactionsFromCache() throws InterruptedException {
        // given
        for (int i = 0; i < 4; i++) {
            transactionCache.put(createTransactionDto(i));
            Thread.sleep(5);
        }

        // then
        assertThat(transactionCache.getCached()
                .size(), is(4));
        Thread.sleep(CLEANUP_INTERVAL * 1000);
        assertThat(transactionCache.getCached()
                .size(), is(3));
        Thread.sleep(CLEANUP_INTERVAL * 1000);
        assertThat(transactionCache.getCached()
                .size(), is(2));
    }

    @Test
    public void shouldClearCache() throws InterruptedException {
        // given
        for (int i = 0; i < 4; i++) {
            transactionCache.put(createTransactionDto(i));
        }

        // when
        transactionCache.cleanCache();

        // then
        assertThat(transactionCache.getCached()
                .size(), is(0));
    }

    private TransactionDto createTransactionDto(int i) {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setTimestamp(LocalDateTime.now()
                .minusSeconds((i + 1) * CLEANUP_INTERVAL));
        transactionDto.setAmount(BigDecimal.TEN);
        return transactionDto;
    }
}
