package com.n26.challenge.statistics.cache;

import com.n26.challenge.statistics.dto.TransactionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class WriteTimeBasedCacheTest {

    private static final long ENTRY_EXPIRATION_TIME = 4L;
    private WriteTimeBasedCache writeTimeBasedCache;

    @BeforeEach
    public void setup() {
        writeTimeBasedCache = new WriteTimeBasedCache(ENTRY_EXPIRATION_TIME);
    }

    @Test
    public void shouldRemoveOutdatedTransactionsFromCache() throws InterruptedException {
        // given
        for (int i = 0; i < 4; i++) {
            writeTimeBasedCache.put(new TransactionDto());
            Thread.sleep(980);
        }

        // then
        assertThat(writeTimeBasedCache.getCached()
                .size(), is(4));
        Thread.sleep(1000);
        assertThat(writeTimeBasedCache.getCached()
                .size(), is(3));
        Thread.sleep(1000);
        assertThat(writeTimeBasedCache.getCached()
                .size(), is(2));
    }

    @Test
    public void shouldClearCache() throws InterruptedException {
        // given
        for (int i = 0; i < 4; i++) {
            writeTimeBasedCache.put(new TransactionDto());
        }
        
        // when
        writeTimeBasedCache.cleanCache();

        // then
        assertThat(writeTimeBasedCache.getCached()
                .size(), is(0));
    }

}
