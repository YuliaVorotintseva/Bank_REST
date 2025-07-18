package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Представление карты для пользователя")
public class UserCardDTO {
    @Schema(description = "ID карты", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Маскированный номер", example = "**** **** **** 1234", accessMode = Schema.AccessMode.READ_ONLY)
    private String maskedNumber;

    @Schema(description = "Срок действия", example = "12/25", accessMode = Schema.AccessMode.READ_ONLY)
    private String expiryDate;

    @Schema(description = "Баланс", example = "1500.50", accessMode = Schema.AccessMode.READ_ONLY)
    private Double balance;

    @Schema(description = "Статус", example = "ACTIVE", allowableValues = {"ACTIVE", "BLOCKED", "EXPIRED"})
    private String status;
}