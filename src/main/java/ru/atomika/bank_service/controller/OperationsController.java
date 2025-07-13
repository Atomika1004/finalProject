package ru.atomika.bank_service.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.atomika.bank_service.entity.Operations;
import ru.atomika.bank_service.service.OperationsService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/operations")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OperationsController {
    private final OperationsService operationsService;

    @GetMapping("/all")
    public List<Operations> getOperationList(@RequestParam(required = false) LocalDate from, @RequestParam(required = false) LocalDate to) {
        return operationsService.findAllOperations(from, to);
    }
}