package com.n26.challenge.statistics.cache;

import com.n26.challenge.statistics.dto.TransactionDto;
import edu.umd.cs.mtc.MultithreadedTestCase;
import edu.umd.cs.mtc.TestFramework;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TimestampBasedCacheMultithreadedTest extends MultithreadedTestCase {

    private static final long CLEANUP_INTERVAL = 1L;
    private static final long ENTRY_EXPIRATION_TIME = 2L;
    private TimestampBasedCache transactionCache;

    @Override
    public void initialize() {
        transactionCache = new TimestampBasedCache(CLEANUP_INTERVAL, ENTRY_EXPIRATION_TIME);
    }

    public void thread1() throws InterruptedException {
        for (int i = 0; i < 20; i++) {
            transactionCache.put(createTransactionDto());
        }
    }

    public void thread2() throws InterruptedException {
        for (int i = 0; i < 20; i++) {
            transactionCache.put(createTransactionDto());
            if (i % 2 == 0) {
                List<BigDecimal> cached = transactionCache.getCached();
                System.out.println(cached.size());
            }
        }
    }

    @Override
    public void finish() {
        assertThat(transactionCache.getCached()
                .size(), is(40));
    }

    @Test
    public void test() throws Throwable {
        TestFramework.runManyTimes(new TimestampBasedCacheMultithreadedTest(), 2);
    }

    private TransactionDto createTransactionDto() {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setTimestamp(LocalDateTime.now());
        transactionDto.setAmount(BigDecimal.TEN);
        return transactionDto;
    }
}
