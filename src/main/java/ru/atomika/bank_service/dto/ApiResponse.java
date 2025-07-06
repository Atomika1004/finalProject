package ru.atomika.bank_service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class ApiResponse {
    Long value;
    String message;
}
