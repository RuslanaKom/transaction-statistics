package com.n26.challenge.statistics.cache;

import com.n26.challenge.statistics.dto.TransactionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@Primary
public class TimestampBasedCache implements TransactionCache {
    private static final Logger logger = LoggerFactory.getLogger(TimestampBasedCache.class);

    private final Map<UUID, TransactionDto> cache = new ConcurrentHashMap<>();

    public TimestampBasedCache(@Value("${cache.cleanup.seconds}") long cleanupInterval, @Value("${cache.expiration.seconds}") long entryExpirationTime) {
        startCacheCleanupThread(cleanupInterval, entryExpirationTime);
    }

    public void put(TransactionDto transactionDto) {
        cache.put(UUID.randomUUID(), transactionDto);
        logger.info("Transaction entry added to cache.");
    }

    public void cleanCache() {
        cache.clear();
        logger.info("Cache cleared.");
    }

    public List<BigDecimal> getCached() {
        logger.info("Extracting values from cache.");
        return cache.values()
                .stream()
                .map(TransactionDto::getAmount)
                .collect(Collectors.toList());
    }

    private void startCacheCleanupThread(long cleanupInterval, long entryExpirationTime) {
        if (entryExpirationTime > 0 && cleanupInterval > 0) {
            Thread cleanupThread = new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(cleanupInterval * 1000);
                    } catch (InterruptedException e) {
                        logger.error("Cache cleanup thread interrupted.", e);
                    }
                    removeExpired(entryExpirationTime);
                }
            });
            cleanupThread.setDaemon(true);
            cleanupThread.start();
        }
    }

    private void removeExpired(long entryExpirationTime) {
        LocalDateTime expirationThreshold = LocalDateTime.now()
                .minusSeconds(entryExpirationTime);
        cache.entrySet()
                .removeIf(entry -> entry.getValue().getTimestamp()
                        .isBefore(expirationThreshold));
    }

}
