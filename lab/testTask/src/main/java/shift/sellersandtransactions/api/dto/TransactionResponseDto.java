package shift.sellersandtransactions.api.dto;

import shift.sellersandtransactions.core.entity.PaymentType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponseDto(
        Long id,
        SellerResponseDto seller,
        BigDecimal amount,
        PaymentType paymentType,
        LocalDateTime transactionDate
) {}