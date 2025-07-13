package ru.atomika.bank_service.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.atomika.bank_service.entity.Account;
import ru.atomika.bank_service.entity.Operations;
import ru.atomika.bank_service.enums.OperationType;
import ru.atomika.bank_service.repository.OperationsRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OperationsServiceTest {

    @Mock
    OperationsRepository operationsRepository;

    @InjectMocks
    OperationsService operationsService;

    @Test
    void saveOperation() {
        Account accountFrom = new Account();
        accountFrom.setId(1L);
        Account accountTo = new Account();
        accountTo.setId(2L);
        BigDecimal amount = BigDecimal.valueOf(100);
        OperationType operationType = OperationType.TRANSFER;

        operationsService.saveOperation(accountFrom, accountTo, operationType, amount);

        verify(operationsRepository, times(1)).save(any(Operations.class));
    }

    @Test
    void findAllOperations_NoDates() {
        List<Operations> expectedOperations = List.of(new Operations(), new Operations());
        when(operationsRepository.findAll()).thenReturn(expectedOperations);

        List<Operations> result = operationsService.findAllOperations(null, null);

        assertEquals(expectedOperations, result);
        verify(operationsRepository).findAll();
    }

    @Test
    void findAllOperations_OnlyFromDate() {
        LocalDate from = LocalDate.of(2025, 7, 10);
        List<Operations> expectedOperations = List.of(new Operations());
        when(operationsRepository.findAllFromDate(from)).thenReturn(expectedOperations);

        List<Operations> result = operationsService.findAllOperations(from, null);

        assertEquals(expectedOperations, result);
        verify(operationsRepository).findAllFromDate(from);
    }

    @Test
    void findAllOperations_OnlyToDate() {
        LocalDate to = LocalDate.of(2025, 7, 20);
        List<Operations> expectedOperations = List.of(new Operations());
        when(operationsRepository.findAllToDate(to)).thenReturn(expectedOperations);

        List<Operations> result = operationsService.findAllOperations(null, to);

        assertEquals(expectedOperations, result);
        verify(operationsRepository).findAllToDate(to);
    }

    @Test
    void findAllOperations_DateRange() {
        LocalDate from = LocalDate.of(2025, 7, 1);
        LocalDate to = LocalDate.of(2025, 7, 31);
        List<Operations> expectedOperations = List.of(new Operations());
        when(operationsRepository.findByDateBetween(from, to)).thenReturn(expectedOperations);

        List<Operations> result = operationsService.findAllOperations(from, to);

        assertEquals(expectedOperations, result);
        verify(operationsRepository).findByDateBetween(from, to);
    }

    @Test
    void findAllOperations_InvalidDateRange() {
        LocalDate from = LocalDate.of(2025, 8, 31);
        LocalDate to = LocalDate.of(2025, 7, 1);

        assertThrows(IllegalArgumentException.class, () ->
                operationsService.findAllOperations(from, to)
        );
    }
}