package com.n26.challenge.statistics.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.n26.challenge.statistics.dto.TransactionDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void testEmptyStatistics() throws Exception {
        mvc.perform(get("/statistics")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.sum").value(BigDecimal.ZERO))
                .andExpect(jsonPath("$.avg").value(BigDecimal.ZERO))
                .andExpect(jsonPath("$.min").value(BigDecimal.ZERO))
                .andExpect(jsonPath("$.max").value(BigDecimal.ZERO))
                .andExpect(jsonPath("$.count").value(0));
    }

    @Test
    public void testStatistics() throws Exception {
        // save transactions
        for (int i = 0; i < 3; i++) {
            mvc.perform(post("/transactions")
                    .content(createTransactionDto())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated());
        }
        // check saved
        mvc.perform(get("/statistics")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.sum").value(BigDecimal.valueOf(30.0)))
                .andExpect(jsonPath("$.avg").value(BigDecimal.valueOf(10.0)))
                .andExpect(jsonPath("$.min").value(BigDecimal.valueOf(10.0)))
                .andExpect(jsonPath("$.max").value(BigDecimal.valueOf(10.0)))
                .andExpect(jsonPath("$.count").value(3));

        // clear stransactions
        mvc.perform(delete("/transactions")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        // check cleared
        mvc.perform(get("/statistics")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.sum").value(BigDecimal.ZERO))
                .andExpect(jsonPath("$.avg").value(BigDecimal.ZERO))
                .andExpect(jsonPath("$.min").value(BigDecimal.ZERO))
                .andExpect(jsonPath("$.max").value(BigDecimal.ZERO))
                .andExpect(jsonPath("$.count").value(0));
    }

    private String createTransactionDto() throws JsonProcessingException {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setAmount(BigDecimal.TEN);
        transactionDto.setTimestamp(LocalDateTime.now());
        String str = transactionDto.toString();
        return str;
    }

}
