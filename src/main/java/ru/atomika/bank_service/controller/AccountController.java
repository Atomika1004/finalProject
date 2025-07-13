package ru.atomika.bank_service.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.atomika.bank_service.dto.ApiResponse;
import ru.atomika.bank_service.dto.TransferDto;
import ru.atomika.bank_service.entity.Account;
import ru.atomika.bank_service.service.AccountService;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/account")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountController {

    AccountService accountService;

    @PostMapping("/crateAccount")
    public ResponseEntity<ApiResponse> crateAccount(@RequestBody Account account) {
        return new ResponseEntity<>(accountService.createAccount(account), HttpStatus.CREATED);
    }

    @GetMapping("/getBalance/{id}")
    public ResponseEntity<ApiResponse> getBalance(@PathVariable Long id) {
        return new ResponseEntity<>(accountService.getAccount(id), HttpStatus.OK);
    }

    @PostMapping("/putMoney/{id}")
    public ResponseEntity<ApiResponse> putMoney(@PathVariable Long id, @RequestParam(name = "amount") BigDecimal amount) {
        return new ResponseEntity<>(accountService.putMoney(id, amount), HttpStatus.OK);
    }

    @PostMapping("/takeMoney/{id}")
    public ResponseEntity<ApiResponse> takeMoney(@PathVariable Long id, @RequestParam(name = "amount") BigDecimal amount) {
        return new ResponseEntity<>(accountService.takeMoney(id, amount), HttpStatus.OK);
    }

    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse> transfer(@RequestBody @Valid TransferDto transferDto) {
        return new ResponseEntity<>(accountService.transferMoney(transferDto), HttpStatus.OK);
    }
}
