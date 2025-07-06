package ru.atomika.bank_service.exception;

public class NotEnoughMoney extends RuntimeException {
    public NotEnoughMoney(String message) {
        super(message);
    }
}
