package shift.sellersandtransactions.api.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public record ClientResponseDto(int phone,
                             String name,
                             String lastName,
                             String middleName,
                             String email,
                             LocalDate birthday,
                             OffsetDateTime creationTime,
                             OffsetDateTime updateTime) {
}
