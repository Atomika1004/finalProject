package ru.atomika.bank_service.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.atomika.bank_service.entity.Account;
import ru.atomika.bank_service.entity.Operations;
import ru.atomika.bank_service.enums.OperationType;
import ru.atomika.bank_service.repository.OperationsRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OperationsService {

    OperationsRepository operationsRepository;

    @Transactional
    public void saveOperation(Account accountFrom, Account accountTo,  OperationType operationType, BigDecimal amount) {
        Operations operation = new Operations();
        operation.setAccount(accountFrom);
        operation.setAccountTransfer(accountTo);
        operation.setDate(LocalDate.now());
        operation.setAmount(amount);
        operation.setOperation(operationType);
        operationsRepository.save(operation);
    }

    @Transactional(readOnly = true)
    public List<Operations> findAllOperations(LocalDate from, LocalDate to) {
        if (Objects.isNull(from) && Objects.isNull(to)) {
            return operationsRepository.findAll();
        } else if (Objects.isNull(to)) {
            return operationsRepository.findAllFromDate(from);
        } else if (Objects.isNull(from)) {
            return operationsRepository.findAllToDate(to);
        }else {
            validateDate(from, to);
            return operationsRepository.findByDateBetween(from, to);
        }
    }

    private void validateDate(LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("Дата начала не может быть позже даты конца промежутка");
        }
    }
}