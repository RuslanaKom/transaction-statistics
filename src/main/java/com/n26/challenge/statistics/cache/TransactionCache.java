package com.n26.challenge.statistics.cache;

import com.n26.challenge.statistics.dto.TransactionDto;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionCache {

    void put(TransactionDto transactionDto);

    void cleanCache();

    List<BigDecimal> getCached();
}
