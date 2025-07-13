package ru.atomika.bank_service.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.atomika.bank_service.dto.ApiResponse;
import ru.atomika.bank_service.dto.TransferDto;
import ru.atomika.bank_service.entity.Account;
import ru.atomika.bank_service.enums.OperationType;
import ru.atomika.bank_service.exception.AccounstNotFoundException;
import ru.atomika.bank_service.exception.NotEnoughMoney;
import ru.atomika.bank_service.repository.AccountRepository;

import java.math.BigDecimal;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AccountService {

    AccountRepository accountRepository;
    OperationsService operationsService;

    @SneakyThrows
    @Transactional
    public ApiResponse createAccount(Account account) {
        if (account.getId() == null) {
            Account saveAccount = accountRepository.save(account);
            log.info("Аккаунт успешно создан. id: {}, balance: {} руб", saveAccount.getId(), saveAccount.getBalance());
            return ApiResponse.builder()
                    .value(saveAccount.getId())
                    .message(String.format("Аккаунт c id: %d успешно создан. Баланс: %s руб",
                            saveAccount.getId(), saveAccount.getBalance()))
                    .build();
        } else {
            Account saveAccount = findAccountById(account.getId());
            saveAccount.setBalance(saveAccount.getBalance());
            accountRepository.save(saveAccount);
            return ApiResponse.builder()
                    .value(saveAccount.getId())
                    .message(String.format("Аккаунт c id: %d успешно обновлен. Баланс: %s руб",
                            saveAccount.getId(), saveAccount.getBalance()))
                    .build();
        }
    }

    @SneakyThrows
    @Transactional(readOnly = true)
    public ApiResponse getAccount(Long accountId) {
        Account account = findAccountById(accountId);
        return ApiResponse.builder()
                .value(1L)
                .message(String.format("Баланс аккаунта с id: %d равен %s руб", account.getId(), account.getBalance()))
                .build();
    }

    @SneakyThrows
    @Transactional
    public ApiResponse putMoney(Long accountId, BigDecimal amount)  {
        try {
            Account account = findAccountById(accountId);
            validateAmount(amount);
            account.setBalance(account.getBalance().add(amount));
            Account savedAccount = accountRepository.save(account);
            operationsService.saveOperation(savedAccount,null, OperationType.PUT, amount);
            return ApiResponse.builder()
                    .value(1L)
                    .message(String.format("На аккаунт с id: %d была добавлена сумма: %s. Баланс равен %s руб",
                            savedAccount.getId(), amount, savedAccount.getBalance()))
                    .build();
        } catch (Exception e) {
            log.error("При добавлении средств произошла ошибка {}", e.getMessage());
            return ApiResponse.builder()
                    .value(0L)
                    .message(String.format("Ошибка при выполнении операции пополнения на аккаунт с id: %d", accountId))
                    .build();
        }
    }

    @SneakyThrows
    @Transactional
    public ApiResponse takeMoney(Long accountId, BigDecimal amount) {
        try {
            Account account = findAccountById(accountId);
            validateAmount(amount);
            if (account.getBalance().compareTo(amount) < 0) {
                throw new NotEnoughMoney(String.format("На аккаунте с id: %d недостаточно средств", accountId));
            }
            account.setBalance(account.getBalance().subtract(amount));
            Account savedAccount = accountRepository.save(account);
            operationsService.saveOperation(savedAccount, null, OperationType.TAKE, amount);
            return ApiResponse.builder()
                    .value(1L)
                    .message(String.format("С аккаунта с id: %d была снята сумма: %s. Баланс равен %s руб",
                            savedAccount.getId(), amount, savedAccount.getBalance()))
                    .build();
        } catch (Exception e) {
            log.error("При снятии средств произошла ошибка {}", e.getMessage());
            return ApiResponse.builder()
                    .value(0L)
                    .message(String.format("Ошибка при выполнении операции снятия средств с аккаунта: %d", accountId))
                    .build();
        }
    }

    @Transactional
    public ApiResponse transferMoney(TransferDto transferDto) {
        Account accountFrom = findAccountById(transferDto.getFromAccountId());
        Account accountTo = findAccountById(transferDto.getToAccountId());
        BigDecimal amount = transferDto.getAmount();

        accountFrom.setBalance(accountFrom.getBalance().subtract(amount));
        accountTo.setBalance(accountTo.getBalance().add(amount));

        accountRepository.save(accountFrom);
        accountRepository.save(accountTo);

        operationsService.saveOperation(accountFrom, accountTo, OperationType.TRANSFER, amount);

        return ApiResponse.builder()
                .value(1L)
                .message(String.format("Перевод с аккаунта %d на аккаунт %d успешно произведен. Сумма перевода %s",
                        transferDto.getFromAccountId(), transferDto.getToAccountId(), amount))
                .build();
    }

    public Account findAccountById(Long accountId) {
        if (accountId == null) {
            throw new IllegalArgumentException("ID is null");
        }
        return accountRepository.findById(accountId).orElseThrow(
                () -> new AccounstNotFoundException( String.format("Аккаунт с id: %d не найден", accountId))
        );
    }

    public void validateAmount(BigDecimal amount) {
        if (Objects.isNull(amount)) {
            throw new IllegalArgumentException("Сумма не может быть null");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Сумма должна быть больше 0");
        }
    }
}
