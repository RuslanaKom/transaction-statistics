package com.n26.challenge.statistics.service;

import com.n26.challenge.statistics.cache.TransactionCache;
import com.n26.challenge.statistics.dto.StatisticsDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StatisticsServiceTest {

    private static final int DEFAULT_SCALE = 2;

    @Mock
    private TransactionCache transactionCache;

    private StatisticsService statisticsService;

    @BeforeEach
    public void setup() {
        statisticsService = new StatisticsService(transactionCache, DEFAULT_SCALE);
    }

    @Test
    public void shouldReturnEmptyStatisticsIfNoCache() {
        // given
        when(transactionCache.getCached()).thenReturn(Collections.emptyList());

        // when
        StatisticsDto result = statisticsService.getStatistics();

        // then
        assertThat(result.getCount(), is(0));
        assertThat(result.getAvg(), is(BigDecimal.ZERO));
        assertThat(result.getMax(), is(BigDecimal.ZERO));
        assertThat(result.getMin(), is(BigDecimal.ZERO));
        assertThat(result.getSum(), is(BigDecimal.ZERO));
    }

    @Test
    public void shouldReturnStatistics() {
        // given
        when(transactionCache.getCached()).thenReturn(createValues());

        // when
        StatisticsDto result = statisticsService.getStatistics();

        // then
        assertThat(result.getCount(), is(3));
        assertThat(result.getAvg(), is(BigDecimal.valueOf(3.67)));
        assertThat(result.getMax(), is(BigDecimal.valueOf(10)
                .setScale(2)));
        assertThat(result.getMin(), is(BigDecimal.valueOf(0)
                .setScale(2)));
        assertThat(result.getSum(), is(BigDecimal.valueOf(11)
                .setScale(2)));
    }

    private List<BigDecimal> createValues() {
        return Arrays.asList(BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.ONE);
    }

}
