package ru.atomika.bank_service.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.atomika.bank_service.dto.ApiResponse;
import ru.atomika.bank_service.entity.Account;
import ru.atomika.bank_service.exception.AccounstNotFoundException;
import ru.atomika.bank_service.exception.AccountExistException;
import ru.atomika.bank_service.exception.NotEnoughMoney;
import ru.atomika.bank_service.repository.AccountRepository;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AccountService {

    AccountRepository accountRepository;

    @SneakyThrows
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
            throw new AccountExistException(String.format("Аккаунт с id: %d уже существует", account.getId()));
        }
    }

    @SneakyThrows
    @Transactional(readOnly = true)
    public ApiResponse getAccount(Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(
                () -> new AccounstNotFoundException( String.format("Аккаунт с id: %d не найден", accountId))
        );
        return ApiResponse.builder()
                .value(1L)
                .message(String.format("Баланс аккаунта с id: %d равен %s руб", account.getId(), account.getBalance()))
                .build();
    }

    @SneakyThrows
    @Transactional
    public ApiResponse putMoney(Long accountId, BigDecimal amount)  {
        try {
            if (amount.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Сумма должна быть больше 0");
            }
            Account account = accountRepository.findById(accountId).orElseThrow(
                    () -> new AccounstNotFoundException( String.format("Аккаунт с id: %d не найден", accountId))
            );
            BigDecimal newBalance = account.getBalance().add(amount);
            account.setBalance(newBalance);
            Account savedAccount = accountRepository.save(account);
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
            if (amount.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Сумма должна быть больше 0");
            }
            Account account = accountRepository.findById(accountId).orElseThrow(
                    () -> new AccounstNotFoundException( String.format("Аккаунт с id: %d не найден", accountId))
            );
            if (account.getBalance().compareTo(amount) < 0) {
                throw new NotEnoughMoney(String.format("На аккаунте с id: %d недостаточно средств", accountId));
            }
            BigDecimal newBalance = account.getBalance().subtract(amount);
            account.setBalance(newBalance);
            Account savedAccount = accountRepository.save(account);
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
}
