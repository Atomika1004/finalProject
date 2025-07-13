package ru.atomika.bank_service.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.A;
import ru.atomika.bank_service.config.PostgresContainer;
import ru.atomika.bank_service.entity.Account;
import ru.atomika.bank_service.entity.Operations;
import ru.atomika.bank_service.enums.OperationType;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = PostgresContainer.class)
@ActiveProfiles("test")
class OperationsRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(OperationsRepositoryTest.class);

    @Autowired
    OperationsRepository operationsRepository;

    @Autowired
    AccountRepository accountRepository;

    Account savingAccount;

    @BeforeEach
    void setUp() {
        operationsRepository.deleteAll();
        accountRepository.deleteAll();

        Account account = new Account();
        account.setBalance(BigDecimal.valueOf(122));
        savingAccount = accountRepository.save(account);

        OperationType operationType = OperationType.PUT;
        BigDecimal amount = BigDecimal.valueOf(100);

        Operations operation = new Operations();
        operation.setAccount(savingAccount);
        operation.setAccountTransfer(null);
        operation.setDate(LocalDate.now());
        operation.setAmount(amount);
        operation.setOperation(operationType);
        operationsRepository.save(operation);
    }

    @Test
    void findByDateBetween() {
        LocalDate from = LocalDate.of(2025, 7, 11);
        LocalDate to = LocalDate.of(2025, 7, 20);

        assertEquals(operationsRepository.findByDateBetween(from, to).size(), 1);
    }

    @Test
    void findAll() {
        assertEquals(operationsRepository.findAll().size(), 1);
    }

    @Test
    void findAllFromDate() {
        LocalDate from = LocalDate.of(2025, 7, 11);

        assertEquals(operationsRepository.findAllFromDate(from).size(), 1);
    }

    @Test
    void findAllToDate() {
        LocalDate to = LocalDate.of(2025, 7, 20);

        assertEquals(operationsRepository.findAllToDate(to).size(), 1);
    }
}