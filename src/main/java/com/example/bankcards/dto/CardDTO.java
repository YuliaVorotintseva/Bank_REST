package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CardDTO {
    @Schema(description = "ID карты", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Маскированный номер карты", example = "**** **** **** 1234", accessMode = Schema.AccessMode.READ_ONLY)
    private String maskedNumber;

    @Schema(description = "Имя владельца", example = "IVAN IVANOV")
    private String ownerName;

    @Schema(description = "Срок действия (MM/yy)", example = "12/25")
    private String expiryDate;

    @Schema(description = "Баланс", example = "1000.00", accessMode = Schema.AccessMode.READ_ONLY)
    private Double balance;

    @Schema(description = "Статус карты", example = "ACTIVE", allowableValues = {"ACTIVE", "BLOCKED", "EXPIRED"})
    private String status;
}