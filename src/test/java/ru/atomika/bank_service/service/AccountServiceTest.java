package ru.atomika.bank_service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.atomika.bank_service.dto.ApiResponse;
import ru.atomika.bank_service.dto.TransferDto;
import ru.atomika.bank_service.entity.Account;
import ru.atomika.bank_service.repository.AccountRepository;
import ru.atomika.bank_service.repository.OperationsRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    AccountRepository accountRepository;

    @Mock
    OperationsRepository operationsRepository;

    @InjectMocks
    AccountService accountService;

    @Mock
    OperationsService operationsService;

    @Test
    void createAccount() {
        Account createdAccount = createAccount(null, BigDecimal.valueOf(100));
        Account returnedAccount = createAccount(1L, BigDecimal.valueOf(100));

        when(accountRepository.save(createdAccount)).thenReturn(returnedAccount);

        ApiResponse response = accountService.createAccount(createdAccount);

        assertNotNull(response);
        assertEquals(response.getValue(), returnedAccount.getId());
        verify(accountRepository, times(1)).save(createdAccount);
    }

    @Test
    void putMoney() {
        Long accountId = 1L;
        BigDecimal initialBalance = BigDecimal.valueOf(100);
        BigDecimal addedAmount = BigDecimal.valueOf(100);
        BigDecimal expectedBalance = initialBalance.add(addedAmount);

        Account existingAccount = createAccount(accountId, initialBalance);
        Account updatedAccount = createAccount(accountId, expectedBalance);

        when(accountRepository.findById(accountId))
                .thenReturn(Optional.of(existingAccount));

        when(accountRepository.save(any(Account.class)))
                .thenReturn(updatedAccount);

        ApiResponse result = accountService.putMoney(accountId, addedAmount);

        assertNotNull(result);
        assertEquals(1L, result.getValue());
        assertEquals(expectedBalance, updatedAccount.getBalance());

        verify(accountRepository).findById(accountId);
        verify(accountRepository).save(existingAccount);
    }

    @Test
    void takeMoney() {
        Long accountId = 1L;
        BigDecimal initialBalance = BigDecimal.valueOf(200);
        BigDecimal addedAmount = BigDecimal.valueOf(100);
        BigDecimal expectedBalance = initialBalance.subtract(addedAmount);

        Account existingAccount = createAccount(accountId, initialBalance);
        Account updatedAccount = createAccount(accountId, expectedBalance);

        when(accountRepository.findById(accountId))
                .thenReturn(Optional.of(existingAccount));

        when(accountRepository.save(any(Account.class)))
                .thenReturn(updatedAccount);

        ApiResponse result = accountService.putMoney(accountId, addedAmount);

        assertNotNull(result);
        assertEquals(1L, result.getValue());
        assertEquals(expectedBalance, updatedAccount.getBalance());

        verify(accountRepository).findById(accountId);
        verify(accountRepository).save(existingAccount);
    }

    @Test
    void transferMoney() {
        Long accountFromId = 1L;
        Long accountToId = 2L;
        BigDecimal initialBalance = BigDecimal.valueOf(1000);
        BigDecimal transferAmount = BigDecimal.valueOf(100);
        BigDecimal afterTransferFromAccount = initialBalance.subtract(transferAmount);
        BigDecimal afterTransferToAccount = initialBalance.add(transferAmount);

        Account existingFromAccount = createAccount(accountFromId, initialBalance);
        Account existingToAccount = createAccount(accountToId, initialBalance);
        Account updatedFromAccount = createAccount(accountFromId, afterTransferFromAccount);
        Account updatedToAccount = createAccount(accountToId, afterTransferToAccount);

        TransferDto transferDto = TransferDto.builder()
                .fromAccountId(accountFromId)
                .toAccountId(accountToId)
                .amount(transferAmount)
                .build();

        when(accountRepository.findById(accountFromId))
                .thenReturn(Optional.of(existingFromAccount));

        when(accountRepository.findById(accountToId))
                .thenReturn(Optional.of(existingToAccount));

        ApiResponse result = accountService.transferMoney(transferDto);

        assertNotNull(result);
        assertEquals(1L, result.getValue());
        assertEquals(updatedFromAccount.getBalance(), afterTransferFromAccount);
        assertEquals(updatedToAccount.getBalance(), afterTransferToAccount);

        verify(accountRepository, times(1)).findById(accountFromId);
        verify(accountRepository, times(1)).findById(accountToId);
        verify(accountRepository, times(1)).save(existingFromAccount);
        verify(accountRepository, times(1)).save(existingToAccount);
    }

    Account createAccount(Long id, BigDecimal balance) {
        Account account = new Account();
        account.setId(id);
        account.setBalance(balance);
        return account;
    }
}