package ru.atomika.bank_service.exception;

public class AccountExistException extends RuntimeException {
    public AccountExistException(String message) {
        super(message);
    }
}
