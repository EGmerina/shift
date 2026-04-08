package shift.sellersandtransactions.api.dto;

import jakarta.validation.constraints.NotBlank;

public record SellerRequestDto(
        @NotBlank(message = "Имя продавца не может быть пустым")
        String name,

        @NotBlank(message = "Контактная информация обязательна")
        String contactInfo
) {}