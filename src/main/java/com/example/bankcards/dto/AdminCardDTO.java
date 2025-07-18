package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDate;

@Data
@Schema(description = "Административное представление карты")
public class AdminCardDTO {
    @Schema(description = "ID карты", example = "1")
    private Long id;

    @Schema(description = "Полный номер карты", example = "1234567890123456")
    private String fullNumber;

    @Schema(description = "Имя владельца", example = "IVAN IVANOV", required = true)
    private String ownerName;

    @Schema(description = "Срок действия", example = "2025-12-31")
    private LocalDate expiryDate;

    @Schema(description = "Баланс", example = "1000.00")
    private Double balance;

    @Schema(description = "Статус", example = "ACTIVE", allowableValues = {"ACTIVE", "BLOCKED", "EXPIRED"})
    private String status;

    @Schema(description = "ID владельца", example = "5")
    private Long userId;
}