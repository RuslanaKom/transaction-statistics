package com.n26.challenge.statistics.controller;

import com.n26.challenge.statistics.dto.StatisticsDto;
import com.n26.challenge.statistics.service.StatisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping
    public ResponseEntity<StatisticsDto> getStatistics() {
        StatisticsDto statisticsDto = statisticsService.getStatistics();
        return ok(statisticsDto);
    }
}
