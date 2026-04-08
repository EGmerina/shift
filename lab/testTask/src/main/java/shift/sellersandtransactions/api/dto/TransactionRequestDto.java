package shift.sellersandtransactions.api.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import shift.sellersandtransactions.core.entity.PaymentType;

import java.math.BigDecimal;

public record TransactionRequestDto(
        @NotNull(message = "ID продавца обязательно для заполнения")
        Long sellerId,

        @NotNull(message = "Сумма транзакции обязательна")
        @Positive(message = "Сумма транзакции должна быть больше нуля")
        BigDecimal amount,

        @NotNull(message = "Тип оплаты обязателен")
        PaymentType paymentType
) {}