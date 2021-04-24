package com.n26.challenge.statistics.service;

import com.n26.challenge.statistics.cache.TransactionCache;
import com.n26.challenge.statistics.dto.StatisticsDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;

@Service
public class StatisticsService {

    private final TransactionCache transactionCache;

    private final int defaultScale;

    public StatisticsService(TransactionCache transactionCache, @Value("${bigdecimal.scale}") int defaultScale) {
        this.transactionCache = transactionCache;
        this.defaultScale = defaultScale;
    }

    public StatisticsDto getStatistics() {
        List<BigDecimal> values = transactionCache.getCached();
        return calculateStatistics(values);
    }

    private StatisticsDto calculateStatistics(List<BigDecimal> values) {
        StatisticsDto statisticsDto = new StatisticsDto();
        if (values.size() < 1) {
            return statisticsDto;
        }
        Collections.sort(values);
        int count = values.size();
        BigDecimal sumNotScaled = getSumNoScale(values);
        statisticsDto.setCount(count);
        statisticsDto.setMax(getMax(values));
        statisticsDto.setMin(getMin(values));
        statisticsDto.setSum(setDefaultScale(sumNotScaled));
        statisticsDto.setAvg(getAvg(sumNotScaled, count));
        return statisticsDto;
    }

    private BigDecimal getAvg(BigDecimal sumNotScaled, int count) {
        return sumNotScaled.divide(BigDecimal.valueOf(count), defaultScale, RoundingMode.HALF_UP);
    }

    private BigDecimal getMax(List<BigDecimal> values) {
        return setDefaultScale(values.get(values.size() - 1));
    }

    private BigDecimal getMin(List<BigDecimal> values) {
        return setDefaultScale(values.get(0));
    }

    private BigDecimal getSumNoScale(List<BigDecimal> values) {
        return values
                .stream()
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
    }

    private BigDecimal setDefaultScale(BigDecimal bigDecimal) {
        return bigDecimal.setScale(defaultScale, RoundingMode.HALF_UP);
    }

}
