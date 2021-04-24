package com.n26.challenge.statistics.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.n26.challenge.statistics.dto.TransactionDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class WriteTimeBasedCache implements TransactionCache {

    private final Cache<UUID, TransactionDto> cache;

    public WriteTimeBasedCache(@Value("${cache.expiration.seconds}") long entryExpirationTime) {
        cache = CacheBuilder.newBuilder()
                .concurrencyLevel(2)
                .expireAfterWrite(Duration.ofSeconds(entryExpirationTime))
                .build();
    }

    public void put(TransactionDto transactionDto) {
        cache.put(UUID.randomUUID(), transactionDto);
    }

    public List<BigDecimal> getCached() {
        return cache.asMap()
                .values()
                .stream()
                .map(TransactionDto::getAmount)
                .collect(Collectors.toList());
    }

    public void cleanCache() {
        cache.invalidateAll();
    }

}
