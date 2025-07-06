package ru.atomika.bank_service.exception;

public class AccounstNotFoundException extends RuntimeException {
    public AccounstNotFoundException(String message) {
        super(message);
    }
}
