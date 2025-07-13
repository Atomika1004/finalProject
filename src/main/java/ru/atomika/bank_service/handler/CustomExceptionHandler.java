package ru.atomika.bank_service.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.atomika.bank_service.exception.AccountExistException;
import ru.atomika.bank_service.exception.AccounstNotFoundException;
import ru.atomika.bank_service.exception.NotEnoughMoney;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(NotEnoughMoney.class)
    public ResponseEntity<String> notEnoughException(NotEnoughMoney exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(String.format(exception.getMessage()));
    }

    @ExceptionHandler(AccounstNotFoundException.class)
    public ResponseEntity<String> notFoundException(AccounstNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(String.format(exception.getMessage()));
    }

    @ExceptionHandler(AccountExistException.class)
    public ResponseEntity<String> accountExistException(AccountExistException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(String.format(exception.getMessage()));
    }
}
