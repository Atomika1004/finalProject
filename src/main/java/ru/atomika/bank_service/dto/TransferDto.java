package ru.atomika.bank_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;


import java.math.BigDecimal;

@Value
@Builder
public class TransferDto {

    @NotNull(message = "Отсутствует аккаунт с которого происходит перевод")
    Long fromAccountId;

    @NotNull(message = "Отсутствует аккаунт на который происходит перевод")
    Long toAccountId;

    @Min(value = 0, message = "Сумма не может быть меньше нуля")
    BigDecimal amount;
}