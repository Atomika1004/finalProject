package ru.atomika.bank_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.atomika.bank_service.config.PostgresContainer;
import ru.atomika.bank_service.dto.TransferDto;
import ru.atomika.bank_service.entity.Account;
import ru.atomika.bank_service.repository.AccountRepository;
import ru.atomika.bank_service.service.AccountService;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = PostgresContainer.class)
@ActiveProfiles("test")
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountService accountService;

    @Test
    void crateAccount() throws Exception {
        Account newAccount = new Account();
        newAccount.setBalance(BigDecimal.valueOf(100));

        mockMvc.perform(post("/api/account/crateAccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newAccount)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.value").value(1))
                .andReturn();
    }

    @Test
    void getBalance() throws Exception {
        Account newAccount = new Account();
        newAccount.setBalance(BigDecimal.valueOf(100));
        accountService.createAccount(newAccount);

        mockMvc.perform(get("/api/account/getBalance/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.value").value(1))
                .andReturn();

    }

    @Test
    void putMoney() throws Exception {
        BigDecimal initialBalance = BigDecimal.valueOf(100);
        BigDecimal addAmount = BigDecimal.valueOf(100);

        Account newAccount = new Account();
        newAccount.setBalance(initialBalance);
        accountService.createAccount(newAccount);

        mockMvc.perform(post("/api/account/putMoney/1")
                        .param("amount", String.valueOf(addAmount))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.value").value(1))
                .andReturn();
    }

    @Test
    void takeMoney() throws Exception {
        BigDecimal initialBalance = BigDecimal.valueOf(100);
        BigDecimal subtractAmount = BigDecimal.valueOf(100);

        Account newAccount = new Account();
        newAccount.setBalance(initialBalance);
        accountService.createAccount(newAccount);

        mockMvc.perform(post("/api/account/takeMoney/1")
                        .param("amount", String.valueOf(subtractAmount))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.value").value(1))
                .andReturn();
    }

    @Test
    void transfer() throws Exception {
        BigDecimal initialBalance = BigDecimal.valueOf(1000);
        BigDecimal transferAmount = BigDecimal.valueOf(100);

        Account fromAccount = new Account();
        fromAccount.setBalance(initialBalance);
        Account toAccount = new Account();
        toAccount.setBalance(initialBalance);

        accountService.createAccount(fromAccount);
        accountService.createAccount(toAccount);

        TransferDto transferDto = TransferDto.builder()
                .fromAccountId(fromAccount.getId())
                .toAccountId(toAccount.getId())
                .amount(transferAmount)
                .build();

        mockMvc.perform(post("/api/account/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(transferDto)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.value").value(1))
                .andReturn();
    }
}