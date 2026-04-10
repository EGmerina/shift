package shift.sellersandtransactions.api.dto;

import java.time.LocalDate;

public record BestPeriodResponseDto(
        Long sellerId,
        int periodInDays,
        LocalDate startDate,
        LocalDate endDate,
        int transactionCount
) {}