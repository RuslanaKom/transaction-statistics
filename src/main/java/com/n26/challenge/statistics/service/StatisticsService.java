package com.n26.challenge.statistics.service;

import com.n26.challenge.statistics.cache.TransactionCache;
import com.n26.challenge.statistics.dto.StatisticsDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
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
        //Collections.sort(values);
        values = sortList(values);
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

    private List<BigDecimal> sortList(List<BigDecimal> list) {
        BigDecimal[] array = list.toArray(BigDecimal[]::new);
        mergeSort(array, array.length);
        return Arrays.asList(array);
    }

    public static void mergeSort(BigDecimal[] a, int n) {
        if (n < 2) {
            return;
        }
        int mid = n / 2;
        BigDecimal[] left = new BigDecimal[mid];
        BigDecimal[] right = new BigDecimal[n - mid];

        for (int i = 0; i < mid; i++) {
            left[i] = a[i];
        }
        for (int i = mid; i < n; i++) {
            right[i - mid] = a[i];
        }
        mergeSort(left, left.length);
        mergeSort(right, right.length);

        merge(a, left, right, left.length, right.length);
    }

    public static void merge(
            BigDecimal[] a, BigDecimal[] left, BigDecimal[] right, int leftLength, int rightLength) {

        int i = 0, j = 0, k = 0;
        while (i < leftLength && j < rightLength) {
            if (left[i].compareTo(right[j]) == -1) {
                a[k++] = left[i++];
            } else {
                a[k++] = right[j++];
            }
        }
        while (i < leftLength) {
            a[k++] = left[i++];
        }
        while (j < rightLength) {
            a[k++] = right[j++];
        }
    }
}
