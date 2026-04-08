package shift.sellersandtransactions.api.dto;

import java.time.LocalDateTime;

public record SellerResponseDto(
        Long id,
        String name,
        String contactInfo,
        LocalDateTime registrationDate
) {}