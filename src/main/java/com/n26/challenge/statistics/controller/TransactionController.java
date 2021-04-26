package com.n26.challenge.statistics.controller;

import com.n26.challenge.statistics.cache.TransactionCache;
import com.n26.challenge.statistics.dto.TransactionDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.ResponseEntity.noContent;

@Validated
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionCache transactionCache;

    public TransactionController(TransactionCache transactionCache) {
        this.transactionCache = transactionCache;
    }

    @PostMapping
    public ResponseEntity createTransaction(@RequestBody @Valid TransactionDto transactionDto) {
        transactionCache.put(transactionDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @DeleteMapping
    public ResponseEntity clearTransactions() {
        transactionCache.cleanCache();
        return noContent().build();
    }
}
